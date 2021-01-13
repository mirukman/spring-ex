package io.mirukman.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig  extends AbstractAnnotationConfigDispatcherServletInitializer {

	/*설정파일 및 DispatcherServlet 매핑*/
	@Override
	protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class, SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");

		int maxFileSize = 20 * 1024 * 1024;
		int maxRequestSize = 40 * 1024 * 1024;
		int fileSizeThreshold = 20 * 1024 * 1024;
		MultipartConfigElement multipartConfig = new MultipartConfigElement("/mnt/c/Users/jchg9/workspace/upload/temp", maxFileSize, maxRequestSize, fileSizeThreshold);
		registration.setMultipartConfig(multipartConfig);
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		return new Filter[] { characterEncodingFilter };
	}
    
}
