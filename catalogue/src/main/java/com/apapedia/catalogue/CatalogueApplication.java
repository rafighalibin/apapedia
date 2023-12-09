package com.apapedia.catalogue;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apapedia.catalogue.dto.CatalogueMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.model.Catalogue;
import com.github.javafaker.Faker;
import com.apapedia.catalogue.service.CatalogueService;
import com.apapedia.catalogue.service.CategoryService;
import java.util.*;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class CatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

	@Bean
 @Transactional
 CommandLineRunner run(CatalogueService catalogueService,  CatalogueMapper catalogueMapper, CategoryService categoryService){
  return args -> {
   var faker = new Faker(new Locale("in-ID"));
   var categoryNames = List.of("Aksesoris Fashion", "Buku & Alat Tulis", "Elektronik", "Fashion Bayi & Anak",
           "Fashion Muslim", "Fotografi", "Hobi & Koleksi", "Jam Tangan", "Perawatan & Kecantikan",
           "Makanan & Minuman", "Otomotif", "Perlengkapan Rumah", "Souvenir & Party Supplies");
    for (int ii = 0; ii < categoryNames.size(); ii++){
        Category category  = new Category();
        category.setName(categoryNames.get(ii));
        categoryService.createCategory(category);
    }
  };
 }
 private byte[] generateFakeImageBytes() {
    // Replace this with your actual image generation logic or use a placeholder image
    // For example, you can use a library like Apache Commons Imaging to create a simple placeholder image.
    return new byte[0];
}

}
