package com.amayadream.webchat.pojo;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * 
 * user
 * 
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {


	/**
	 * 
	 */
	private Integer userId;


	/**
	 * 用户名
	 */
	private String userName;


	/**
	 * 密码
	 */
	private String password;


	/**
	 * 昵称
	 */
	private String nickName;


	/**
	 * 性别
	 */
	private Integer sex;


	/**
	 * 年龄
	 */
	private Integer age;


	/**
	 * 头像
	 */
	private String avatarUrl;


	/**
	 * 简介
	 */
	private String profile;


	/**
	 * 注册时间
	 */
	private String firsttime;


	/**
	 * 最后登录时间
	 */
	private String lasttime;


	/**
	 * 账号状态(1正常 0禁用)
	 */
	private Integer status;


	/**
	 * 用户级别：0：管理员，1:非管理员
	 */
	private Integer level;

}
