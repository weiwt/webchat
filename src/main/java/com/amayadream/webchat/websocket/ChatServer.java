package com.amayadream.webchat.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amayadream.webchat.constant.ChatSubjectEnum;
import com.amayadream.webchat.pojo.Chat;
import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.ChatQueryService;
import com.amayadream.webchat.utils.ResultDTO;
import com.sun.deploy.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * websocket服务
 * @author  :  Amayadream
 * @time   :  2016.01.08 09:50
 */
@ServerEndpoint(value = "/chatServer", configurator = HttpSessionConfigurator.class)
public class ChatServer {
    private static AtomicInteger onlineCount = new AtomicInteger(); //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static CopyOnWriteArraySet<ChatServer> webSocketSet = new CopyOnWriteArraySet<ChatServer>();
    private Session session;    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private static Set<User> userSet = new TreeSet<>();   //在线列表,记录用户名称
    private static ConcurrentHashMap<Integer,Session> routetab = new ConcurrentHashMap<>();  //用户名和websocket的session绑定的路由表
    private HttpSession httpSession;
    private User currentUser;

    private ChatQueryService chatQueryService;
    {
        chatQueryService = (ChatQueryService) ContextLoader.getCurrentWebApplicationContext().getBean("chatQueryService");
    }
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        User user = (User) httpSession.getAttribute("user");    //获取当前用户
        this.currentUser = user;
        userSet.add(user);           //将用户名加入在线列表
        routetab.put(user.getUserId(), session);   //将用户名和session绑定到路由表
        String message = getMessage("[" + user.getUserName() + "]加入聊天室,当前在线人数为"+getOnlineCount()+"位", "notice",  new ArrayList<>(userSet));
        broadcast(message);     //广播
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        userSet.remove(currentUser);        //从在线列表移除这个用户
        routetab.remove(currentUser.getUserId());
        String message = getMessage("[" + currentUser.getUserName() +"]离开了聊天室,当前在线人数为"+getOnlineCount()+"位", "notice", new ArrayList<>(userSet));
        broadcast(message);         //广播
    }

    /**
     * 接收客户端的message,判断是否有接收人而选择进行广播还是指定发送
     * @param _message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String _message) {
        JSONObject chat = JSON.parseObject(_message);
        JSONObject message = JSON.parseObject(chat.get("message").toString());
        if(message.get("to") == null || message.get("to").equals("")){      //如果to为空,则广播;如果不为空,则对指定的用户发送消息
            broadcast(_message);
        }else{
            String [] userlist = message.get("to").toString().split(",");
            String from = (String) message.get("from");
            singleSend(_message, (Session) routetab.get(Integer.parseInt(from)));      //发送给自己,这个别忘了
            for(String user : userlist){
                if(!user.equals(message.get("from"))){
                    singleSend(_message, (Session) routetab.get(Integer.parseInt(user)));     //分别发送给每个指定用户
                    Chat chatBean = Chat.builder()
                            .fromId(Integer.parseInt(from))
                            .toId(Integer.parseInt(user))
                            .subject(ChatSubjectEnum.TEXT.getValue())
                            .message(_message)
                            .type(0)
                            .build();
                    saveChat(chatBean);
                }
            }
        }
    }

    /**
     * 发生错误时调用
     * @param error
     */
    @OnError
    public void onError(Throwable error){
        error.printStackTrace();
    }

    /**
     * 广播消息
     * @param message
     */
    public void broadcast(String message){
        for(ChatServer chat: webSocketSet){
            try {
                chat.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 对特定用户发送消息
     * @param message
     * @param session
     */
    public void singleSend(String message, Session session){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 组装返回给前台的消息
     * @param message   交互信息
     * @param type      信息类型
     * @param list      在线列表
     * @return
     */
    public String getMessage(String message, String type, List list){
        JSONObject member = new JSONObject();
        member.put("message", message);
        member.put("type", type);
        member.put("list", list);
        return member.toString();
    }

    public  int getOnlineCount() {
        return onlineCount.get();
    }

    public  void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public  void subOnlineCount() {
        onlineCount.decrementAndGet();
    }


    private List<Chat> queryChats(Integer userId){
        ResultDTO<List<Chat>> listResultDTO = chatQueryService.queryChatByUserId(userId);

        return listResultDTO.getModel();
    }

    private void saveChat(Chat chat){
        chatQueryService.addChats(chat);
    }
}
