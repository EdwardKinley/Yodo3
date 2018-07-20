package controllers;

import db.DBHelper;
import models.Book;
import models.User;
import models.enums.Format;
import models.enums.Genre;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class BooksController {

    public BooksController() { this.setupEndpoints(); }

    private void setupEndpoints () {

//        VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

        get("/books", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "/templates/books/index.vtl");
            List<Book> book = DBHelper.getAll(Book.class);
            model.put("books", book);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/books/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/books/create.vtl");
            List<User> users = DBHelper.getAll(User.class);
            model.put("users", users);
            List<Genre> genres = Arrays.asList(Genre.values());
            model.put("genres", genres);
            List<Format> formats = Arrays.asList(Format.values());
            model.put("formats", formats);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("books/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/books/show.vtl");
            int bookId = Integer.parseInt(req.params(":id"));
            Book book = DBHelper.find(bookId, Book.class);
            model.put("book", book);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/books", (req, res) -> {
            String title = req.queryParams("title");
            String description = req.queryParams("description");
            int price = Integer.valueOf(req.queryParams("price"));
            String imageUrl = req.queryParams("imageUrl");
            int userId = Integer.valueOf(req.queryParams("user"));
            User user = DBHelper.find(userId, User.class);
            Genre genre = Genre.valueOf(req.queryParams("genre"));
            Format format = Format.valueOf(req.queryParams("format"));
            Book newBook = new Book(title, description, price, imageUrl, user, genre, format);
            DBHelper.save(newBook);
            res.redirect("/books");
            return null;
        }, new VelocityTemplateEngine());

        get("books/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int bookId = Integer.parseInt(req.params(":id"));
            Book book = DBHelper.find(bookId, Book.class);
            model.put("book", book);
            model.put("template", "templates/books/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

//        post("/books/:id", (req, res) -> {
////            Map<String, Object> model = new HashMap<>();
//            String bookname = req.queryParams("bookname");
//            int credit = Integer.valueOf(req.queryParams("credit"));
//            int bookId = Integer.parseInt(req.params(":id"));
//            Book book = DBHelper.find(bookId, Book.class);
//            book.setBookname(bookname);
//            book.setCredit(credit);
//            DBHelper.update(book);
//            res.redirect("/books");
//            return null;
//        }, new VelocityTemplateEngine());

        post("/books/:id/delete", (req, res) -> {
            int bookId = Integer.parseInt(req.params(":id"));
            Book book = DBHelper.find(bookId, Book.class);
            DBHelper.delete(book);
            res.redirect("/books");
            return null;
        }, new VelocityTemplateEngine());


    }
}