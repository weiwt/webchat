package com.amayadream.webchat.pojo;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * 
 * ow_group
 * 
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {


	/**
	 * 
	 */
	private Integer groupId;


	/**
	 * 
	 */
	private String groupName;



}
