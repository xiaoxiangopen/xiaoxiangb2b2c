package net.xiaoxiangshop.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.xiaoxiangshop.entity.ProductImage;
import net.xiaoxiangshop.util.JsonUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import com.fasterxml.jackson.core.type.TypeReference;

@MappedJdbcTypes({JdbcType.LONGVARCHAR})
public class ProductImageHandler extends BaseTypeHandler<List<ProductImage>>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<ProductImage> parameter, JdbcType jdbcType) throws SQLException {
		if (parameter != null) {
			ps.setString(i, JsonUtils.toJson(parameter));
		}
	}

	@Override
	public List<ProductImage> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String dbData = rs.getString(columnName);
		if (StringUtils.isEmpty(dbData)) {
			return null;
		}
        return JsonUtils.toObject(dbData, new TypeReference<List<ProductImage>>() { });
	}

	@Override
	public List<ProductImage> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String dbData = rs.getString(columnIndex);
		if (StringUtils.isEmpty(dbData)) {
			return null;
		}
        return JsonUtils.toObject(dbData, new TypeReference<List<ProductImage>>() { });
	}

	@Override
	public List<ProductImage> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String dbData = cs.getString(columnIndex);
		if (StringUtils.isEmpty(dbData)) {
			return null;
		}
        return JsonUtils.toObject(dbData, new TypeReference<List<ProductImage>>() { });
	}

}
