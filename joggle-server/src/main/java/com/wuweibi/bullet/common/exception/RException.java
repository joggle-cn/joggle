package com.wuweibi.bullet.common.exception;

import com.wuweibi.bullet.entity.api.R;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 * （处理事务回滚、msg返回）
 *
 * @author system
 * @date 2020年11月30日16:21:57
 */
@NoArgsConstructor
public class RException extends RuntimeException {

	private R result;

	public RException(String message) {
		super(message);
		this.result = R.fail(message);
	}

	public RException(R r) {
		super(r.getMsg());
		this.result = r;
	}

	public R getResult(){
		return this.result;
	}


}
