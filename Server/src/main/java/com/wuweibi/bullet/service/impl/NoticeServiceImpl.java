package com.wuweibi.bullet.service.impl;

import com.faceinner.service.NoticeService;
import com.faceinner.thirdparty.feition.MicroFeition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * @author marker
 * @version 1.0
 */
@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired private MicroFeition fetionClient;
	
	
	@Override
	public void send(final String content) {


		// 暂时这样异步处理
		new Thread(){
			public void run() {
				fetionClient.send("13518135097", content);
			};
		}.start();
	}

	
}
