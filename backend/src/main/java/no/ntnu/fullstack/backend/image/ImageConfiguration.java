package no.ntnu.fullstack.backend.image;

import java.nio.file.Paths;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfiguration implements WebMvcConfigurer {
  @Value("${application.image.upload.dir}")
  private String uploadDir;

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    String uploadPath = Paths.get(uploadDir).toFile().getAbsolutePath();
    registry.addResourceHandler("/" + uploadPath + "/").addResourceLocations("file:" + uploadPath + "/");
  }
}
