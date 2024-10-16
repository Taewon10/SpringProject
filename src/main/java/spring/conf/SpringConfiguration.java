package spring.conf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration //이 클래스가 Spring 설정 클래스임을 지정.
@EnableTransactionManagement // 트랜젝션 관리를 활성화해 트랜젝션 매니저가 사용할수 있게 함.
@PropertySource("classpath:spring/db.properties") //db.properties 파일의 설정 값을 클래스 내에 주입할수 있도록 함.
@MapperScan("user.dao")
public class SpringConfiguration {

	/*
	 * @Value("com.mysql.cj.jdbc.Driver") private String driver;
	 * 
	 * @Value("jdbc:mysql://localhost:3306/mydb?serverTimezone=Aisa/Seoul") private
	 * String url;
	 * 
	 * @Value("root") private String username;
	 * 
	 * @Value("1234") private String password;
	 */
	//@Value 는 db.properties 파일에서 불러온 값을 각 필드에 주입.
	//DB연결에 필요한 드라이버, URL, username, password를 설정하는 부분.

	@Value("${jdbc.driver}")
	private String driver;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;
	
	@Autowired
	private ApplicationContext con;
	
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		System.out.println(driver);
		basicDataSource.setDriverClassName(driver);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);
		return basicDataSource;
		//@Bean 은 이 메서드를 Spring 컨텍스트에 빈으로 등록.
		//BasicDataSource : DBCP2를 사용해 DB 연결 풀을 설정.
		//드라이버 클래스, DB연결 URL, DB username, password 설정.
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("spring/mybatis-config.xml"));
		
		//1개
		//sqlSessionFactoryBean.setMapperLocations(new ClassPathResource("mapper/userMapper.xml"));
		
		//Mapper.xml이 2개 이상일때
		/*
		sqlSessionFactoryBean.setMapperLocations(
				new ClassPathResource("mapper/userMapper.xml"),
				new ClassPathResource("mapper/userUploadMapper.xml"));
		*/
		sqlSessionFactoryBean.setMapperLocations(con.getResources(("classpath:mapper/*Mapper.xml")));
		
		sqlSessionFactoryBean.setTypeAliasesPackage("*.bean");
		
		return sqlSessionFactoryBean.getObject(); //SqlSessionFactory 변환
		
		
		//SqlSessionFactoryBean : MyBatis의 SqlSessionFActory를 설정하는데 사용.
		//setDataSource(dataSource()) : DB 연결을 위한 DataSource 설정.
		//setConfigLocation : MyBatis 설정파일의 경로 지정.
		//setMapperLocation : 매퍼 XML 파일의 위치 설정.
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
		return sqlSessionTemplate;
		//SqlSessionTemplate : MyBatis의 SQL 세션을 관리. SQL실행, 트랜젝션 관리, 매퍼 인터페이스 구현을 제공.
		//sqlSessionFactory() : 위에서 생성한 SqlSessionFactory를 주입하여 MyBatis 세션을 설정.
		}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
		return dataSourceTransactionManager;
		//DataSourceTransactionManager : JDBC용 트랜젝션 관리자를 설정. Spring에서 트랜젝션을 관리할수 있도록 해줌.
		//dataSource() : 위에서 생성한 DataSource 빈을 사용하여 DB 연결 풀을 이용해 트랜젝션을 관리.
	}

}
