package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Entity - ERP返回结果
 * 
 */
@Entity
public class ErpResult extends BaseEntity<ErpResult>  {

	//接口类型
	public  String erpType;
	//	发送时间
	public Date sendTime;
	//发送内容
	public  String sendText;
	//	返回时间
	public  Date resultTime;
	//	返回内容
	public  String resultText;
	//	返回状态
	public  String resultCode;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	//订单号
	public  String  orderSn;

	public String getErpType() {
		return erpType;
	}

	public void setErpType(String erpType) {
		this.erpType = erpType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendText() {
		return sendText;
	}

	public void setSendText(String sendText) {
		this.sendText = sendText;
	}

	public Date getResultTime() {
		return resultTime;
	}

	public void setResultTime(Date resultTime) {
		this.resultTime = resultTime;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}