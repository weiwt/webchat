package com.amayadream.webchat.pojo;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Date;

/**
 * 
 * ow_chats
 * 
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {


	/**
	 * 主键
	 */
	private Integer id;


	/**
	 * 来自我：0是，1：不是
	 */
	private Integer fromId;


	/**
	 * 发送人 或是接收人 id
	 */
	private Integer toId;


	/**
	 * 
	 */
	private String message;


	/**
	 * 消息类型
	 */
	private Integer subject;


	/**
	 * 单聊或群聊：0单聊，1：群聊
	 */
	private Integer type;

	private Date updateTime;

}
