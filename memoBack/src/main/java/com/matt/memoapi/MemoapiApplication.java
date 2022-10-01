package com.matt.memoapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.matt.memoapi.domain.Memo;
import com.matt.memoapi.repository.MemoRepository;

@SpringBootApplication
public class MemoapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoapiApplication.class, args);


	}

}


// Palier aux problemes CORS policy
@Configuration
class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
        
    }
}

@Component
class MemoCommandLineRunner implements CommandLineRunner{
    

	@Override
	public void run(String... args) throws Exception {

        create("Yolo", "Yolo les amis");
	}	

    private void create (String title, String content){
        Memo memo = new Memo();
        memo.setDescription(content);
        memo.setTitle(title);
        memoRepository.save(memo);
    }
	
	@Autowired 
	MemoRepository memoRepository;
}