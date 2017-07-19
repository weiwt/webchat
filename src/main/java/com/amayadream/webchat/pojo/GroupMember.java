package com.amayadream.webchat.pojo;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * 
 * ow_group_member
 * 
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {


	/**
	 * 
	 */
	private Integer id;


	/**
	 * 用户id
	 */
	private Integer userId;


	/**
	 * 用户所在组id
	 */
	private Integer groupId;



}
