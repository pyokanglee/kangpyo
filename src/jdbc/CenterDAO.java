package jdbc;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
 
public class CenterDAO {
 
    Connection  con;
    //Statement stmt;
    PreparedStatement stmt;
    ResultSet rs;
    String sql;
    
    public CenterDAO() {
        try {
			/*
			 * String url="jdbc:mariadb://localhost:3306/green_db"; 
			 * String id = "green";
			 * String pw = "123456";
			 * 
			 * Class.forName("org.mariadb.jdbc.Driver");
			 * 
			 * con = DriverManager.getConnection(url,id,pw);
			 */
        	
        	Context init = new InitialContext();  
        	Context env= (Context)init.lookup("java:/comp/env");
        	DataSource ds= (DataSource)env.lookup("mmm");
        	
        	con= ds.getConnection();
        	

            
            //stmt = con.createStatement();
    
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    public ArrayList<CenterDTO> list(String cate){
        ArrayList<CenterDTO> res = new ArrayList<CenterDTO>();
        
        sql = "select * from center where cate = ?";
        
        try {
            //rs = stmt.executeQuery(sql);
            
            stmt = con.prepareStatement(sql);
            
            stmt.setString(1, cate);
            //stmt.setString(2, pw);
                    
            rs =stmt.executeQuery();
            
            
            
            while(rs.next()) {
                CenterDTO dto = new CenterDTO();
                dto.setId(rs.getInt("id"));
                dto.setPname(rs.getString("pname"));
                dto.setCate(rs.getString("cate"));
                dto.setRegdate(rs.getDate("regdate"));
                dto.setTitle(rs.getString("title"));
                dto.setFile(rs.getString("file"));
                res.add(dto);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close();
        }
        
        return res;
    }
    

    public CenterDTO detail(String id){
        CenterDTO dto = null;
        
        //sql = "select * from center where id = "+id;
        sql = "select * from center where id = ?";
        try {
            //rs = stmt.executeQuery(sql);
            stmt = con.prepareStatement(sql);
            
            stmt.setString(1, id);
            //stmt.setString(2, pw);
                    
            rs =stmt.executeQuery();
            
            
            
            
            if(rs.next()) {
                 dto = new CenterDTO();
                dto.setId(rs.getInt("id"));
                dto.setPname(rs.getString("pname"));
                dto.setTitle(rs.getString("title"));
                dto.setCate(rs.getString("cate"));
                dto.setRegdate(rs.getDate("regdate"));
                dto.setContent(rs.getString("content"));
                dto.setFile(rs.getString("file"));
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close();
        }
        
        return dto;
    }
    
    
    public int insert(CenterDTO dto){
        
        int res = -1;
        
//        sql = "insert into center ("
//                + "pname, title, cate, content, regdate) values ( '"
//                +dto.getPname()+ "','"
//                +dto.getTitle()+"','"
//                +dto.getCate()+"','"
//                +dto.getContent()+"',sysdate())";
      
        sql = "insert into center ("
                + "pname, title, cate, content, regdate,  file) "
                + "values "
                + "(?,?,?,?,?,?)";
        

        
        try {
            System.out.println(sql);

            //stmt.executeUpdate(sql);
            stmt = con.prepareStatement(sql);
            
            stmt.setString(1, dto.getPname());
            stmt.setString(2, dto.getTitle());
            stmt.setString(3, dto.getCate());
            stmt.setString(4, dto.getContent());
            stmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setString(6, dto.getFile());
            //stmt.executeUpdate();
            rs =stmt.executeQuery();
            
            
            sql = "select max(id) from center";
            
            System.out.println(sql);
            
            //rs =stmt.executeQuery();
            rs = stmt.executeQuery(sql);
            
            rs.next();
            res = rs.getInt(1);
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close();
        }
        
        return res;
    }
    
    
    void close() {
        if(rs!=null) try {rs.close();} catch (SQLException e) {}
        if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
        if(con!=null) try {con.close();} catch (SQLException e) {}
    }
    
}
