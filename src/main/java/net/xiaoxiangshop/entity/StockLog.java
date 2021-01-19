package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 库存记录
 * 
 */
@Entity
public class StockLog extends BaseEntity<StockLog> {

	private static final long serialVersionUID = 5550452675645134078L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 入库
		 */
		STOCK_IN(0),

		/**
		 * 出库
		 */
		STOCK_OUT(1);
		
		private int value;

		Type(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 类型
	 */
	private StockLog.Type type;

	/**
	 * 入库数量
	 */
	private Integer inQuantity;

	/**
	 * 出库数量
	 */
	private Integer outQuantity;

	/**
	 * 当前库存
	 */
	private Integer stock;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * SKU
	 */
	@TableField(exist = false)
	private Sku sku;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public StockLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(StockLog.Type type) {
		this.type = type;
	}

	/**
	 * 获取入库数量
	 * 
	 * @return 入库数量
	 */
	public Integer getInQuantity() {
		return inQuantity;
	}

	/**
	 * 设置入库数量
	 * 
	 * @param inQuantity
	 *            入库数量
	 */
	public void setInQuantity(Integer inQuantity) {
		this.inQuantity = inQuantity;
	}

	/**
	 * 获取出库数量
	 * 
	 * @return 出库数量
	 */
	public Integer getOutQuantity() {
		return outQuantity;
	}

	/**
	 * 设置出库数量
	 * 
	 * @param outQuantity
	 *            出库数量
	 */
	public void setOutQuantity(Integer outQuantity) {
		this.outQuantity = outQuantity;
	}

	/**
	 * 获取当前库存
	 * 
	 * @return 当前库存
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * 设置当前库存
	 * 
	 * @param stock
	 *            当前库存
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取SKU
	 * 
	 * @return SKU
	 */
	public Sku getSku() {
		return sku;
	}

	/**
	 * 设置SKU
	 * 
	 * @param sku
	 *            SKU
	 */
	public void setSku(Sku sku) {
		this.sku = sku;
	}

}