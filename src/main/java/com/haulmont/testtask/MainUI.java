package com.haulmont.testtask;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.dao.BookDao;
import com.haulmont.testtask.dao.GenreDao;
import com.haulmont.testtask.dao.impl.AuthorDaoImpl;
import com.haulmont.testtask.dao.impl.BookDaoImpl;
import com.haulmont.testtask.dao.impl.GenreDaoImpl;
import com.haulmont.testtask.entity.Author;
import com.haulmont.testtask.entity.Book;
import com.haulmont.testtask.entity.Genre;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.EnumSet;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private AuthorDao authorDao;
    private BookDao bookDao;
    private GenreDao genreDao;

    private Author author;
    private Book book;
    private Genre genre;

    private final VerticalLayout mainLayout = new VerticalLayout();
    private final TabSheet tabSheet = new TabSheet();

    private final Grid bookGrid = new Grid();
    private final VerticalLayout bookTabLayout = new VerticalLayout();
    private final VerticalLayout bookGridLayout = new VerticalLayout();
    private final HorizontalLayout bookTabMenu = new HorizontalLayout();
    private final Button addBookButton = new Button("Add Book");
    private final Button editBookButton = new Button("Edit Book");
    private final Button deleteBookButton = new Button("Delete Book");
    private final TextField nameBookField = new TextField();
    private final NativeSelect authorSelect = new NativeSelect();
    private final NativeSelect genreSelect = new NativeSelect();
    private final Button filterBookButton = new Button("Filter");
    private final Button showAllBookButton = new Button("Show All");

    private final Grid genreGrid = new Grid();
    private final VerticalLayout genreTabLayout = new VerticalLayout();
    private final VerticalLayout genreGridLayout = new VerticalLayout();
    private final HorizontalLayout genreTabMenu = new HorizontalLayout();
    private final Button addGenreButton = new Button("Add Genre");
    private final Button editGenreButton = new Button("Edit Genre");
    private final Button deleteGenreButton = new Button("Delete Genre");
    private final Button statGenreButton = new Button("Genre Statistic");

    private final Grid authorGrid = new Grid();
    private final VerticalLayout authorTabLayout = new VerticalLayout();
    private final VerticalLayout authorGridLayout = new VerticalLayout();
    private final HorizontalLayout authorTabMenu = new HorizontalLayout();
    private final Button addAuthorButton = new Button("Add Author");
    private final Button editAuthorButton = new Button("Edit Author");
    private final Button deleteAuthorButton = new Button("Delete Author");

    @Override
    protected void init(VaadinRequest request) {

        authorDao = AuthorDaoImpl.getInstance();
        bookDao = BookDaoImpl.getInstance();
        genreDao = GenreDaoImpl.getInstance();

        initBookLayout();
        initGenreLayout();
        initAuthorLayout();
        initListeners();

        bookTabLayout.setCaption("Books");
        genreTabLayout.setCaption("Genres");
        authorTabLayout.setCaption("Authors");
        tabSheet.addTab(bookTabLayout);
        tabSheet.addTab(genreTabLayout);
        tabSheet.addTab(authorTabLayout);

        mainLayout.addComponent(tabSheet);

        setContent(mainLayout);

    }

    private void updateBookGrid() {
        deleteBookButton.setEnabled(false);
        editBookButton.setEnabled(false);
        bookGrid.setContainerDataSource(new BeanItemContainer<>(Book.class, bookDao.getAllBooks()));
    }

    private void updateGenreGrid() {
        deleteGenreButton.setEnabled(false);
        editGenreButton.setEnabled(false);
        statGenreButton.setEnabled(false);
        genreGrid.setContainerDataSource(new BeanItemContainer<>(Genre.class, genreDao.getAllGenres()));
    }

    private void updateAuthorGrid() {
        deleteAuthorButton.setEnabled(false);
        editAuthorButton.setEnabled(false);
        authorGrid.setContainerDataSource(new BeanItemContainer<>(Author.class, authorDao.getAllAuthors()));
    }

    private void initBookLayout(){
        bookGrid.setColumns("name", "publisher", "author", "genre", "publicationDate", "publicationCity");
        bookGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        updateBookGrid();
        bookGrid.setSizeFull();

        authorSelect.setContainerDataSource(new BeanItemContainer<>(Author.class, authorDao.getAllAuthors()));
        genreSelect.setContainerDataSource(new BeanItemContainer<>(Genre.class, genreDao.getAllGenres()));
//        genreSelect.setContainerDataSource(new BeanItemContainer<>(Book.Publisher.class, EnumSet.allOf(Book.Publisher.class)));


        bookTabMenu.addComponent(addBookButton);
        bookTabMenu.addComponent(editBookButton);
        bookTabMenu.addComponent(deleteBookButton);
        bookTabMenu.addComponent(nameBookField);
        bookTabMenu.addComponent(authorSelect);
        bookTabMenu.addComponent(genreSelect);
        bookTabMenu.addComponent(filterBookButton);
        bookTabMenu.addComponent(showAllBookButton);

        bookGridLayout.addComponent(bookGrid);
        bookTabLayout.addComponent(bookTabMenu);
        bookTabLayout.addComponent(bookGridLayout);
    }

    private void initGenreLayout(){
        genreGrid.setColumns("name");
        genreGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        updateGenreGrid();
        genreGrid.setSizeFull();

        genreTabMenu.addComponent(addGenreButton);
        genreTabMenu.addComponent(editGenreButton);
        genreTabMenu.addComponent(deleteGenreButton);
        genreTabMenu.addComponent(statGenreButton);

        genreGridLayout.addComponent(genreGrid);
        genreTabLayout.addComponent(genreTabMenu);
        genreTabLayout.addComponent(genreGridLayout);
    }

    private void initAuthorLayout(){
        authorGrid.setColumns("firstname", "surname", "lastname");
        authorGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        updateAuthorGrid();
        authorGrid.setSizeFull();

        authorTabMenu.addComponent(addAuthorButton);
        authorTabMenu.addComponent(editAuthorButton);
        authorTabMenu.addComponent(deleteAuthorButton);

        authorGridLayout.addComponent(authorGrid);
        authorTabLayout.addComponent(authorTabMenu);
        authorTabLayout.addComponent(authorGridLayout);
    }

    private void initListeners(){

        bookGrid.addSelectionListener(l ->{
            book = (Book) bookGrid.getSelectedRow();
            editBookButton.setEnabled(true);
            deleteBookButton.setEnabled(true);
        });

        genreGrid.addSelectionListener(l ->{
            genre = (Genre) genreGrid.getSelectedRow();
            editGenreButton.setEnabled(true);
            deleteGenreButton.setEnabled(true);
            statGenreButton.setEnabled(true);
        });

        authorGrid.addSelectionListener(l ->{
            author = (Author) authorGrid.getSelectedRow();
            editAuthorButton.setEnabled(true);
            deleteAuthorButton.setEnabled(true);
        });

        statGenreButton.addClickListener(l ->{
            System.out.println(genreDao.getGenreCount(genre));
        });


    }
}