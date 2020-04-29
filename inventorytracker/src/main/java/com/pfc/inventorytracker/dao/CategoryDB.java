/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Category;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pfcar
 */
@Repository
public class CategoryDB implements CategoryDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Category> getAllCategories() {
        return jdbc.query("SELECT * FROM category", new CategoryMapper());
    }

    @Override
    public Category getCategoryById(int id) {
        try{
           return jdbc.queryForObject("SELECT * FROM category WHERE id = ?", new CategoryMapper(), id);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        jdbc.update("INSERT INTO category (name) VALUES (?)",
                category.getName());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        category.setId(newId);
        return category;
    }

    @Override
    public void updateCategory(Category category) {
        jdbc.update("UPDATE category SET name = ? WHERE id = ?",
                category.getName(),
                category.getId());
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        jdbc.update("DELETE FROM item_category WHERE categoryId = ?", id);
        jdbc.update("DELETE FROM category WHERE id = ?", id);
    }

    public static final class CategoryMapper implements RowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int arg1) throws SQLException {
            Category c = new Category();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            return c;
        }

    }
}
