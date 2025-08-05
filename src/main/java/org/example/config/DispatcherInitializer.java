package org.example.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// Bu klass Spring MVC DispatcherServlet'ini initsializatsiya qilish uchun ishlatiladi.
// U WebMvcConfiguration.class'ni servlet konfiguratsiyasi sifatida yuklaydi.
public class DispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Root kontekst konfiguratsiyasi (biznes logikasi, xizmatlar va boshqalar) uchun.
    // Hozircha null, lekin kelajakda qo'shimcha konfiguratsiyalar qo'shilishi mumkin.
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    // Servlet kontekst konfiguratsiyasi (WebMvc, controllerlar, view resolverlar) uchun.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebMvcConfiguration.class};
    }

    // Barcha so'rovlarni DispatcherServlet tomonidan qayta ishlash uchun sozlaydi.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}