package in.onepartner.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Part;

import in.onepartner.bean.CategoryBean;
import in.onepartner.bean.ProductsBean;
import in.onepartner.bean.UserBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.exception.DatabaseException;
import in.onepartner.exception.DuplicateRecordException;
import in.onepartner.exception.RecordNotFoundException;
import in.onepartner.util.EmailBuilder;
import in.onepartner.util.EmailMessage;
import in.onepartner.util.EmailUtility;
import in.onepartner.util.JDBCDataSource;
import in.onepartner.util.S3Handler;

public class UserModel {

	/**
	 * Find next PK of user
	 * 
	 * @throws DatabaseException
	 */
	private long roleId;

	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * returns next pk
	 */

	public Integer nextPK() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_user");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public Integer nextCategoryPK() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from category");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception in getting categoryPK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public static Integer nextProductPK() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from product_table");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception in getting ProductsPK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	/**
	 * @param id          : long id
	 * @param old         password : String oldPassword
	 * @param newpassword : String newPassword
	 * @throws DatabaseException
	 */

	public boolean changePassword(Long id, String oldPassword, String newPassword)
			throws RecordNotFoundException, ApplicationException {

		boolean flag = false;
		UserBean beanExist = null;

		beanExist = findByPK(id);

		if (beanExist != null && beanExist.getPassword().equals(oldPassword)) {

			beanExist.setPassword(newPassword);

			try {
				updatePartner(beanExist);
			} catch (DuplicateRecordException e) {
				e.printStackTrace();
				throw new ApplicationException("LoginId already exists");
			}
			flag = true;
		} else {
			throw new RecordNotFoundException("Old password is invalid");
		}

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("login", beanExist.getLogin());
		map.put("password", beanExist.getPassword());
		map.put("name", beanExist.getName());

		String message = EmailBuilder.getChangePasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(beanExist.getLogin());
		msg.setSubject("ONEPartner Password has been changed Successfully");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return flag;

	}

	/**
	 * Find By mobile number
	 * 
	 * @param login : get parameter
	 * @return bean
	 * @throws ApplicationException
	 */

	public UserBean findByMobileNumber(String mobileNumber) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_user where mobile_no=?");
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, mobileNumber);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by mobile number");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public long add(UserBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		int pk = 0;
		UserBean existbean = findByLogin(bean.getLogin());
		UserBean existbean2 = findByMobileNumber(bean.getMobileNo());

		if (existbean != null) {
			throw new DuplicateRecordException("Login-Id already exists");
		}

		if (existbean2 != null) {
			throw new DuplicateRecordException("Mobile number already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("insert into st_user values(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getLogin());
			pstmt.setString(4, bean.getPassword());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setLong(6, bean.getRoleId());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
			if (bean.getDescription() == null) {

				pstmt.setNull(11, java.sql.Types.BIGINT);
			} else {
				pstmt.setString(11, bean.getDescription());

			}

			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public CategoryBean findByCategory(String cat) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_user where category=?");
		CategoryBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, cat);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by category user model");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public void updateCategory(CategoryBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn
					.prepareStatement("update category set category=?, marketplaceid=? where id=?");
			pstmt.setString(1, bean.getCategory());
			pstmt.setInt(2, bean.getMarketPlaceId());
			pstmt.setLong(3, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();

				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update UserModel ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void updateEmailSenderUser(UserBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("update emailsenderuser set login=?, password=?");
			pstmt.setString(1, bean.getLogin());
			pstmt.setString(2, bean.getPassword());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();

				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update UserModel ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void updatePartner(UserBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_user set `name`=?, login=?, password=?, mobile_no=?, role_id=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=?, description=? where id=?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getLogin());
			pstmt.setString(3, bean.getPassword());
			pstmt.setString(4, bean.getMobileNo());
			pstmt.setLong(5, bean.getRoleId());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setString(10, bean.getDescription());
			pstmt.setLong(11, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();

				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update UserModel ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public long addCategory(CategoryBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		int pk = 0;

		CategoryBean existbean = findCategoryByName(bean.getCategory());

		if (existbean != null) {
			throw new DuplicateRecordException("Category already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextCategoryPK();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("insert into category values(?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getCategory());
			pstmt.setInt(3, bean.getMarketPlaceId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.getMessage();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.getMessage();
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void updateProducts(ProductsBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"update product_table set product_name=?, description=?, partnershipoffer=?, formlink=?, imageId=?  where id=?");
			pstmt.setString(1, bean.getProductName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getPartnershipOffer());
			pstmt.setString(4, bean.getFormLink());
			pstmt.setString(5, bean.getImageId());
			pstmt.setLong(6, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();

				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update UserModel ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public long addProductCategory(String idd, long pk) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("insert into product_category values(?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, idd);
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add products");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public long addProducts(ProductsBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		int pk = 0;

		ProductsBean existbean = findProductsByName(bean.getProductName());

		if (existbean != null) {
			throw new DuplicateRecordException("Product already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextProductPK();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("insert into product_table values(?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getProductName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getPartnershipOffer());
			pstmt.setString(5, bean.getFormLink());
			pstmt.setString(6, bean.getImageId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}

			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in add products");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	public long registerUser(UserBean bean) throws ApplicationException, DuplicateRecordException {

		long pk = add(bean);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", bean.getLogin());
		map.put("password", bean.getPassword());

		String message = EmailBuilder.getUserRegistrationMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(bean.getLogin());
		msg.setSubject("Registration is successful for ONEPartner");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);
		return pk;
	}

	public UserBean findByPK(long pk) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_user where id=?");
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public CategoryBean findCategoryByName(String catName) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from category where category=?");
		CategoryBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, catName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				bean.setMarketPlaceId(rs.getInt(3));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public ProductsBean findByPKProducts(long pk) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from product_table where id=?");
		ProductsBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
				bean.setCategories(createCategoryBeans(bean.getId()));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public UserBean authenticate(String login, String password) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_user where login = ? and password= ?");
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));

			}
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in get roles");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	public List search(UserBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List<UserBean> search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_user where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and `name` like '%" + bean.getName() + "%'");
			}
			if (bean.getLogin() != null && bean.getLogin().length() > 0) {
				sql.append(" and login like '%" + bean.getLogin() + "%'");
			}
			if (bean.getPassword() != null && bean.getPassword().length() > 0) {
				sql.append(" and password like '%" + bean.getPassword() + "%'");
			}

			if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
				sql.append(" and mobile_no = " + bean.getMobileNo());
			}
			if (bean.getRoleId() > 0) {
				sql.append(" and role_id= " + bean.getRoleId());
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" and description like '%" + bean.getDescription() + "%'");
			}
		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));
				list.add(bean);
			}
			rs.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List<UserBean> searchInGetMethod(int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_user");

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			UserBean bean = null;
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));
				list.add(bean);
			}
			rs.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List<UserBean> searchSpecificUser(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_user where 1=1 ");

		if (bean != null) {

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and `name` like '%" + bean.getName() + "%'");
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" or login like '%" + bean.getName() + "%'");
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" or mobile_no like '%" + bean.getName() + "%'");
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" or password like '%" + bean.getName() + "%'");
			}

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List<ProductsBean> searchSpecificProducts(ProductsBean bean, int pageNo, int pageSize)
			throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from product_table where 1=1 ");

		if (bean != null) {

			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" and id = " + bean.getId());
			}

			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" or product_name like '%" + bean.getProductName() + "%'");
			}
			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" or description like '%" + bean.getProductName() + "%'");
			}

			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" or partnershipoffer like '%" + bean.getProductName() + "%'");
			}
			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" or formlink like '%" + bean.getProductName() + "%'");
			}
			if (bean.getImageId() != null && bean.getImageId().length() > 0) {
				sql.append(" or imageid like '" + bean.getImageId() + "%'");
			}

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println(sql);
		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
				bean.setCategories(createCategoryBeans(bean.getId()));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List searchCategory(CategoryBean bean) throws ApplicationException {
		return searchCategory(bean, 0, 0);
	}

	public List searchCategory(CategoryBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from category where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getCategory() != null && bean.getCategory().length() > 0) {
				sql.append(" and category like '" + bean.getCategory() + "%'");
			}
			if (bean.getMarketPlaceId() > 0) {
				sql.append(" and marketplaceid = " + bean.getMarketPlaceId());
			}

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List<CategoryBean> searchSpecificCategory(CategoryBean bean, int pageNo, int pageSize)
			throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from category where 1=1 ");

		if (bean != null) {
			if (bean.getCategory() != null && bean.getCategory().length() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getCategory() != null && bean.getCategory().length() > 0) {
				sql.append(" or category like '" + bean.getCategory() + "%'");
			}
			if (bean.getCategory() != null && bean.getCategory().length() > 0) {
				sql.append(" or marketplaceid like '" + bean.getCategory() + "%'");
			}

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList<CategoryBean> list = new ArrayList<CategoryBean>();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				list.add(bean);
			}
			System.out.println(sql);
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List<ProductsBean> searchProducts(ProductsBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from product_table where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" and product_name like '%" + bean.getProductName() + "%'");
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" and description like '%" + bean.getDescription() + "%'");
			}

			if (bean.getPartnershipOffer() != null && bean.getPartnershipOffer().length() > 0) {
				sql.append(" and partnershipoffer like '%" + bean.getPartnershipOffer() + "%'");
			}
			if (bean.getFormLink() != null && bean.getFormLink().length() > 0) {
				sql.append(" and formlink like '%" + bean.getFormLink() + "%'");
			}

			System.out.println(sql);

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
				bean.setCategories(createCategoryBeans(bean.getId()));
				list.add(bean);
			}

			rs.close();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Products");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List<ProductsBean> searchAllProducts(ProductsBean bean, int pageNo, int pageSize)
			throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from product_table where 1=1");

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList<ProductsBean> list = new ArrayList<ProductsBean>();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
				bean.setCategories(createCategoryBeans(bean.getId()));
				list.add(bean);
			}

			rs.close();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Products");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public String getProductFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;

	}

	/**
	 * Get List of User
	 * 
	 * @return list : List of User
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List<CategoryBean> categoryList() throws ApplicationException {
		return categoryList(0, 0);
	}

	public List<ProductsBean> productsList() throws ApplicationException {
		return productsList(0, 0);
	}

	/**
	 * Get List of User with pagination
	 * 
	 * @return list : List of users
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List<CategoryBean> categoryList(int pageNo, int pageSize) throws ApplicationException {
		List<CategoryBean> list = new ArrayList<CategoryBean>();
		StringBuffer sql = new StringBuffer("select * from category");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CategoryBean bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				bean.setMarketPlaceId(rs.getInt(3));

				list.add(bean);
			}

			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

	public List<CategoryBean> categoryListHighlightOMarketPlace(int pageNo, int pageSize) throws ApplicationException {
		List<CategoryBean> list = new ArrayList<CategoryBean>();
		StringBuffer sql = new StringBuffer("select * from category where 1=1 and marketplaceid='1'");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CategoryBean bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				list.add(bean);
			}

			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

	public List<CategoryBean> highlightOnMarketPlaceCategoryList(int pageNo, int pageSize) throws ApplicationException {
		List<CategoryBean> list = new ArrayList<CategoryBean>();
		StringBuffer sql = new StringBuffer("select * from category where 1=1 and marketplaceid='1'");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CategoryBean bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				list.add(bean);
			}

			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

	public List<ProductsBean> productsList(int pageNo, int pageSize) throws ApplicationException {
		ArrayList<ProductsBean> list = new ArrayList<ProductsBean>();
		StringBuffer sql = new StringBuffer("select * from product_table");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductsBean bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
				list.add(bean);
			}

			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of product");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

	public ProductsBean findParentCategory(long pk) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select parent_category_id from category where id=?");

		ProductsBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting category by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public static List<ProductsBean> findProductsByCategoryFK(String[] ids) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select distinct pt.product_name, pt.description, pt.partnershipOffer, "
				+ "pt.formLink, pt.image_url, pt.imageid , pt.id" + " from product_table pt \r\n"
				+ "	join product_category pc on pt.id=pc.product_id\r\n"
				+ "	join category c on c.id = pc.category_id\r\n" + "	where c.id in ("
				+ UserModel.createInClauseWithCounts(ids) + ")");

		ProductsBean bean = null;
		Connection conn = null;
		ArrayList list = new ArrayList();

		System.out.println(sql);
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			int count = 1;
			for (String str : ids) {
				pstmt.setString(count, str);
				count++;
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setProductName(rs.getString(1));
				bean.setDescription(rs.getString(2));
				bean.setPartnershipOffer(rs.getString(3));
				bean.setFormLink(rs.getString(4));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
				bean.setCategories(createCategoryBeans(rs.getInt(7)));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting findProductsByCategoryFK by ids");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public static List<CategoryBean> createCategoryBeans(long productId) throws ApplicationException {

//		List<Long> productIds = products.stream().
//				map((product)-> product.getId()).
//				collect(Collectors.toList());

		StringBuffer sql = new StringBuffer("select distinct id, category from \r\n" + "	product_category pc \r\n"
				+ "	join category c on c.id = pc.category_id\r\n" + "	where  pc.product_id in ( ? )");

		// '" + bean.getDescription() + "%'"

		CategoryBean bean = null;
		Connection conn = null;
		List<CategoryBean> list = new ArrayList<CategoryBean>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			int count = 1;
			pstmt.setLong(count, productId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getInt(1));
				bean.setCategory(rs.getString(2));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting Products by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	private static String createInClauseWithCount(List<Long> productIds) {
		List<String> strings = new ArrayList();
		for (Long long1 : productIds) {
			strings.add("?");
		}
		return String.join(",", strings);

	}

	private static String createInClauseWithCounts(String[] ids) {
		List<String> strings = new ArrayList();
		for (String long1 : ids) {
			strings.add("?");
		}
		return String.join(",", strings);

	}

	public ProductsBean findProductsByPK(long pk) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from product_table where id=?");
		ProductsBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting findProductsByPK by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public CategoryBean findByPKCategory(long pk) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from category where id=?");
		CategoryBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				bean.setMarketPlaceId(rs.getInt(3));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public ProductsBean findProductsByName(String productName) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from product_table where product_name=?");
		ProductsBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, productName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductsBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setPartnershipOffer(rs.getString(4));
				bean.setFormLink(rs.getString(5));
				bean.setImageURL(S3Handler.getUrl(rs.getString(6)));
				bean.setImageId(rs.getString(6));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting findProductsByPK by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List findCommonCategory(long pk) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from category where common_category=?");
		CategoryBean bean = null;
		Connection conn = null;
		ArrayList list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CategoryBean();
				bean.setId(rs.getLong(1));
				bean.setCategory(rs.getString(2));
				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting commonCategory by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_user");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {
		UserBean userData = findByLogin(login);
		boolean flag = false;

		if (userData == null) {
			throw new RecordNotFoundException("Email-ID does not exists !");

		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLogin());
		map.put("password", userData.getPassword());
		map.put("name", userData.getName());
		String message = EmailBuilder.getForgetPasswordMessage(map);
		EmailMessage msg = new EmailMessage();
		msg.setTo(login);
		msg.setSubject("Password reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		EmailUtility.sendMail(msg);
		flag = true;

		return flag;
	}

	public UserBean findByLogin(String login) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_user where login=?");
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, login);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setLogin(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setMobileNo(rs.getString(5));
				bean.setRoleId(rs.getLong(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				bean.setDescription(rs.getString(11));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by login user model");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public static UserBean getEmailSenderUser() throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from emailsenderuser");
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setLogin(rs.getString(1));
				bean.setPassword(rs.getString(2));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting EmailSenderUser");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public void delete(UserBean bean) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("delete from st_user where id=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public static void deleteAllCategoriesFromProduct_CaterogyTable(long prodId) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM product_category WHERE product_id=?");
			pstmt.setLong(1, prodId);
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in deleteAllCategoriesFromProduct_CaterogyTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteCategory(CategoryBean bean) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("delete from category where id=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete category");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteProduct(ProductsBean bean) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("delete from product_table where id=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Product");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

}
