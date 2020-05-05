/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.CategoryDB.CategoryMapper;
import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class ItemDB implements ItemDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Item> getAllItems() {
        List<Item> items = jdbc.query("SELECT * FROM item", new ItemMapper());
        items = addCategoriesForItems(items);
        return items;
    }

    @Override
    @Transactional
    public Item addItem(Item item) {
        jdbc.update("INSERT INTO item (name, nickname, description, price) VALUES (?,?,?,?)",
                item.getName(),
                item.getNickname(),
                item.getDescription(),
                item.getPrice());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        item.setId(newId);

        insertItemCategory(item);

        return item;
    }

    @Override
    public Item getItemById(int id) {
        try {
            Item item = new Item();
            item = jdbc.queryForObject("SELECT * FROM item WHERE id = ?", new ItemMapper(), id);
            item.setCategories(getCategoriesForItem(item));
            return item;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateItem(Item item) {
        jdbc.update("UPDATE item SET name = ?, nickname = ?, description = ?, price = ? "
                + "WHERE id = ?",
                item.getName(),
                item.getNickname(),
                item.getDescription(),
                item.getPrice(),
                item.getId());
        jdbc.update("DELETE FROM item_category WHERE itemId + ?", item.getId());
        insertItemCategory(item);
    }

    @Override
    @Transactional
    public void deleteItem(int id) {
        jdbc.update("DELETE FROM item_category WHERE itemId = ?", id);
        jdbc.update("DELETE FROM location_item WHERE itemId = ?", id);
        jdbc.update("DELETE FROM request_item WHERE itemId = ?", id);
        jdbc.update("DELETE FROM item WHERE id=?", id);
                
    }

    @Override
    public List<Item> getAllItemsByCategory(Category category) {
        List<Item> items = jdbc.query("SELECT i.* FROM item i "
                + "JOIN item_category ic ON i.id = ic.itemId WHERE ic.categoryId =?",
                new ItemMapper(), category.getId());
        items = addCategoriesForItems(items);
        return items;
    }

    private List<Item> addCategoriesForItems(List<Item> items) {
        for (Item i : items) {
            i.setCategories(getCategoriesForItem(i));
        }
        return items;
    }

    private Set<Category> getCategoriesForItem(Item i) {
        List<Category> categories = jdbc.query("SELECT c.* FROM category c "
                + "JOIN item_category ic ON c.id = ic.categoryId WHERE itemId = ?", new CategoryMapper(), i.getId());
        Set<Category> categorySet = new HashSet<>();
        for (Category c : categories) {
            categorySet.add(c);
        }
        return categorySet;
    }

    private void insertItemCategory(Item item) {
        for (Category c : item.getCategories()) {
            jdbc.update("INSERT INTO item_category(itemId, categoryId) VALUES(?,?)",
                    item.getId(),
                    c.getId());
        }
    }
    
    

    public static final class ItemMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(ResultSet rs, int arg1) throws SQLException {
            Item i = new Item();
            i.setId(rs.getInt("id"));
            i.setName(rs.getString("name"));
            i.setDescription(rs.getString("description"));
            i.setNickname(rs.getString("nickname"));
            i.setPrice(rs.getBigDecimal("price"));
            return i;
        }

    }
    
    public static final class LocationItemMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(ResultSet rs, int arg1) throws SQLException {
            Item i = new Item();
            i.setId(rs.getInt("id"));
            i.setName(rs.getString("name"));
            i.setDescription(rs.getString("description"));
            i.setNickname(rs.getString("nickname"));
            i.setPrice(rs.getBigDecimal("price"));
            i.setInInventory(rs.getInt("inInventory"));
            i.setMax(rs.getInt("max"));
            i.setMin(rs.getInt("min"));
            return i;
        }

    }
    
    public static final class RequestItemMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(ResultSet rs, int arg1) throws SQLException {
            Item i = new Item();
            i.setId(rs.getInt("id"));
            i.setName(rs.getString("name"));
            i.setDescription(rs.getString("description"));
            i.setNickname(rs.getString("nickname"));
            i.setPrice(rs.getBigDecimal("price"));
            i.setQuantity(rs.getInt("quantity"));
            return i;
        }

    }

}
