package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hiki.academyfinal.dao.CategoryDao;
import com.hiki.academyfinal.dto.CategoryDto;

@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    @Autowired
    private CategoryDao categoryDao;

    // 🔥 카테고리 등록
    @PostMapping
    public void insertCategory(@RequestBody CategoryDto categoriesDto) {
        categoryDao.insert(categoriesDto);
    }
    //카테고리 조회
    @GetMapping
    public List<CategoryDto> list(){
    	return categoryDao.selectList();
    }
    //카테고리 수정
    @PutMapping("/{categoryNo}")
    public void updateCategory(@PathVariable int categoryNo, @RequestBody CategoryDto dto) {
        dto.setCategoryNo(categoryNo); // URL로 받은 값 설정
        categoryDao.update(dto);
    }
    //카테고리 삭제
    @DeleteMapping("/{categoryNo}")
    public void deleteCategory(@PathVariable int categoryNo) {
        categoryDao.delete(categoryNo);
    }
    
    
    
    
    
}
