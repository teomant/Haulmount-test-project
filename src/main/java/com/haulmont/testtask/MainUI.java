package com.haulmont.testtask;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.dao.BookDao;
import com.haulmont.testtask.dao.GenreDao;
import com.haulmont.testtask.dao.impl.AuthorDaoImpl;
import com.haulmont.testtask.dao.impl.BookDaoImpl;
import com.haulmont.testtask.dao.impl.GenreDaoImpl;
import com.haulmont.testtask.entity.Author;
import com.haulmont.testtask.entity.Book;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private AuthorDao authorDao;
    private BookDao bookDao;
    private GenreDao genreDao;


    private final Grid authorGrid = new Grid();

    @Override
    protected void init(VaadinRequest request) {

        authorDao = AuthorDaoImpl.getInstance();
        bookDao = BookDaoImpl.getInstance();
        genreDao = GenreDaoImpl.getInstance();

        authorGrid.setColumns("name", "publisher", "author", "genre", "publicationDate", "publicationCity");
        updateAuthorTable();
        authorGrid.setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.addComponent(authorGrid);

//        layout.addComponent(new Label("Main UI"));

        setContent(layout);
    }

    private void updateAuthorTable() {
        Author author =authorDao.getAllAuthors().get(0);
        authorGrid.setContainerDataSource(new BeanItemContainer<>(Book.class, bookDao.filterByAuthor(author)));
    }
}