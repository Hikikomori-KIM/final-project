package com.hiki.academyfinal.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hiki.academyfinal.dto.websocket.MessageViewDto;
import com.hiki.academyfinal.vo.websocket.ChatResponseVO;
import com.hiki.academyfinal.vo.websocket.MessageVO;
import com.hiki.academyfinal.vo.websocket.SystemMessageVO;


@Component
public class MemberMessageConverter {

	public List<MessageVO> convertMessageFormat(List<MessageViewDto> list, String usersId) {
		List<MessageVO> convertList = new ArrayList<>();
		
		for(MessageViewDto messageViewDto : list) {
			switch(messageViewDto.getMessageType()) {
			case "CHAT" :
				convertList.add(ChatResponseVO.builder()
							.messageNo(messageViewDto.getMessageNo())
							.usersId(messageViewDto.getMessageSender())
							.usersName(messageViewDto.getSenderName())
							.usersType(messageViewDto.getSenderType())
							.type("CHAT")
							.content(messageViewDto.getMessageContent())
							.time(messageViewDto.getMessageTime().toLocalDateTime())
						.build());
						break;
			case "SYSTEM" :
				convertList.add(SystemMessageVO.builder()
							.messageNo(messageViewDto.getMessageNo())
							.type("SYSTEM")
							.content(messageViewDto.getMessageContent())
							.time(messageViewDto.getMessageTime().toLocalDateTime())
						.build());
						break;
			}
		}
		return convertList;
	}
}
