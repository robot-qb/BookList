package com.casper.testdrivendevelopment.test;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.casper.testdrivendevelopment.data.BookSaver;
import com.casper.testdrivendevelopment.data.model.Book;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookSaverTest {

    private BookSaver bookKeeper;
    private Context context;
    @Before
    public void setUp() throws Exception {
        context= InstrumentationRegistry.getTargetContext();
        bookKeeper=new BookSaver(context);
        bookKeeper.load();//测试前读取数据

    }

    @After
    public void tearDown() throws Exception {
        bookKeeper.save();//测试后保存数据
    }

    @Test
    public void getBooks() {
        Assert.assertNotNull(bookKeeper.getBooks());
        BookSaver bookSaver=new BookSaver(context);
        Assert.assertNotNull(bookKeeper.getBooks());
        Assert.assertEquals(0,bookSaver.getBooks().size());

    }

    @Test
    public void saveThenLoad() {
        BookSaver bookSaverTest=new BookSaver(context);
        Assert.assertEquals(0,bookSaverTest.getBooks().size());
        Book book=new Book("test book",1.23,123);
        bookSaverTest.getBooks().add(book);
        book=new Book("test book2",1.24,124);
        bookSaverTest.getBooks().add(book);
        bookSaverTest.save();

        BookSaver bookSaverLoader=new BookSaver(context);
        bookSaverLoader.load();
        Assert.assertEquals(bookSaverTest.getBooks().size(),bookSaverLoader.getBooks().size());
        for(int i=0;i<bookSaverTest.getBooks().size();i++){
            Book bookThis=bookSaverTest.getBooks().get(i);
            Book bookThat=bookSaverLoader.getBooks().get(i);
            Assert.assertEquals(bookThat.getCoverResourceId(),bookThis.getCoverResourceId());
            Assert.assertEquals(bookThat.getTitle(),bookThis.getTitle());
            Assert.assertEquals(bookThat.getPrice(),bookThis.getPrice(),1e-4);
        }
    }

    @Test
    public void saveEmptyThenLoad() {
        BookSaver bookSaverTest=new BookSaver(context);
        Assert.assertEquals(0,bookSaverTest.getBooks().size());
        bookSaverTest.save();

        BookSaver bookSaverLoader=new BookSaver(context);
        bookSaverLoader.load();
        Assert.assertEquals(bookSaverTest.getBooks().size(),bookSaverLoader.getBooks().size());

    }
}