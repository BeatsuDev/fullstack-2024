package no.ntnu.fullstack.backend.image;

import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfiguration implements WebMvcConfigurer {
  @Value("${application.image.upload.dir}")
  private String UPLOAD_DIR;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String uploadPath = Paths.get(UPLOAD_DIR).toFile().getAbsolutePath();

    registry
        .addResourceHandler("/" + UPLOAD_DIR + "/**")
        .addResourceLocations("file:" + uploadPath + "/");
  }
}
