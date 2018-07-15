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
import com.haulmont.testtask.pages.AuthorPage;
import com.haulmont.testtask.pages.BookPage;
import com.haulmont.testtask.pages.GenrePage;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.DateToLongConverter;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private final HorizontalLayout bookTabMenu = new HorizontalLayout();
    private final HorizontalLayout filterTabMenu = new HorizontalLayout();
    private final HorizontalLayout filterButtonsTabMenu = new HorizontalLayout();
    private final Button addBookButton = new Button("Add Book");
    private final Button editBookButton = new Button("Edit Book");
    private final Button deleteBookButton = new Button("Delete Book");
    private final TextField nameBookField = new TextField("Filter: Name:");
    private final NativeSelect authorSelect = new NativeSelect("Author:");
    private final NativeSelect publisherSelect = new NativeSelect("Publisher:");
    private final Button filterBookButton = new Button("Filter");
    private final Button showAllBookButton = new Button("Show All");

    private final Grid genreGrid = new Grid();
    private final VerticalLayout genreTabLayout = new VerticalLayout();
    private final HorizontalLayout genreTabMenu = new HorizontalLayout();
    private final Button addGenreButton = new Button("Add Genre");
    private final Button editGenreButton = new Button("Edit Genre");
    private final Button deleteGenreButton = new Button("Delete Genre");
    private final Button statGenreButton = new Button("Genre Statistic");

    private final Grid authorGrid = new Grid();
    private final VerticalLayout authorTabLayout = new VerticalLayout();
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
        book=null;
        bookGrid.setContainerDataSource(new BeanItemContainer<>(Book.class, bookDao.getAllBooks()));
    }

    private void filtredBookGrid(Predicate<Book> predicate){
        deleteBookButton.setEnabled(false);
        editBookButton.setEnabled(false);
        book=null;
        bookGrid.setContainerDataSource(new BeanItemContainer<>(Book.class,
                bookDao.getAllBooks().stream().filter(predicate).collect(Collectors.toList())));
    }

    private void updateGenreGrid() {
        deleteGenreButton.setEnabled(false);
        editGenreButton.setEnabled(false);
        statGenreButton.setEnabled(false);
        genre=null;
        genreGrid.setContainerDataSource(new BeanItemContainer<>(Genre.class, genreDao.getAllGenres()));
    }

    private void updateAuthorGrid() {
        deleteAuthorButton.setEnabled(false);
        editAuthorButton.setEnabled(false);
        author=null;
        authorGrid.setContainerDataSource(new BeanItemContainer<>(Author.class, authorDao.getAllAuthors()));
    }

    private void initBookLayout(){
        bookGrid.setColumns("name", "publisher", "author", "genre", "publicationCity");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        bookGrid.addColumn("publicationDate", Date.class);
        bookGrid.getColumn("publicationDate").setRenderer(new DateRenderer(sdf));
        bookGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        updateBookGrid();
        bookGrid.setSizeFull();

        authorSelect.setContainerDataSource(new BeanItemContainer<>(Author.class, authorDao.getAllAuthors()));
        publisherSelect.setContainerDataSource(new BeanItemContainer<>(Book.Publisher.class, EnumSet.allOf(Book.Publisher.class)));

        authorSelect.setNullSelectionAllowed(true);
        publisherSelect.setNullSelectionAllowed(true);
        authorSelect.setNullSelectionItemId("Any");
        publisherSelect.setNullSelectionItemId("Any");

        bookTabMenu.addComponents(addBookButton,editBookButton,deleteBookButton);
        filterTabMenu.addComponents(nameBookField,
                authorSelect,publisherSelect);
        filterButtonsTabMenu.addComponents(filterBookButton,showAllBookButton);
        bookTabLayout.addComponents(bookTabMenu,filterTabMenu,filterButtonsTabMenu,bookGrid);
    }

    private void initGenreLayout(){
        genreGrid.setColumns("name");
        genreGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        updateGenreGrid();
        genreGrid.setSizeFull();

        genreTabMenu.addComponents(addGenreButton,editGenreButton,deleteGenreButton,statGenreButton);
        genreTabLayout.addComponents(genreTabMenu,genreGrid);
    }

    private void initAuthorLayout(){
        authorGrid.setColumns("firstname", "surname", "lastname");
        authorGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        updateAuthorGrid();
        authorGrid.setSizeFull();

        authorTabMenu.addComponents(addAuthorButton,editAuthorButton,deleteAuthorButton);
        authorTabLayout.addComponents(authorTabMenu,authorGrid);
    }

    private void initListeners(){

        //Grid listeners
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

        //Button listners

        showAllBookButton.addClickListener(l ->{
            updateBookGrid();
        });

        filterBookButton.addClickListener(l ->{

            Predicate<Book> bookNamePredicate = book1 -> {
                Pattern pattern = Pattern.compile(".*"+nameBookField.getValue()+".*");
                return pattern.matcher(book1.getName()).matches();
            };
            Predicate<Book> bookAuthorPredicate = book1 -> {
                return authorSelect.getValue() == null? true :
                        book1.getAuthor().equals(authorSelect.getValue());
            };
            Predicate<Book> bookPublisherPredicate = book1 -> {
                return publisherSelect.getValue() == null? true :
                        book1.getPublisher().equals( publisherSelect.getValue());
            };
            filtredBookGrid(bookNamePredicate.and(bookAuthorPredicate).and(bookPublisherPredicate));

        });

        statGenreButton.addClickListener(l ->{
            System.out.println(genreDao.getGenreCount(genre));
            Notification notification = new Notification(
                    genreDao.getGenreCount(genre)+" books are "+genre.getName()
            );
            notification.setDelayMsec(1500);
            notification.show(Page.getCurrent());
        });

        editGenreButton.addClickListener(l ->{
            GenrePage page = new GenrePage(genre);
            addWindow(page);
            page.addCloseListener(e -> updateGenreGrid());
        });

        addGenreButton.addClickListener(l ->{
            GenrePage page = new GenrePage(null);
            addWindow(page);
            page.addCloseListener(e -> updateGenreGrid());
        });

        editAuthorButton.addClickListener(l ->{
            AuthorPage page = new AuthorPage(author);
            addWindow(page);
            page.addCloseListener(e -> updateAuthorGrid());
        });

        addAuthorButton.addClickListener(l ->{
            AuthorPage page = new AuthorPage(null);
            addWindow(page);
            page.addCloseListener(e -> updateAuthorGrid());
        });

        editBookButton.addClickListener(l ->{
            BookPage page = new BookPage(book);
            addWindow(page);
            page.addCloseListener(e -> updateBookGrid());
        });

        addBookButton.addClickListener(l ->{
            BookPage page = new BookPage(null);
            addWindow(page);
            page.addCloseListener(e -> updateBookGrid());
        });

        deleteBookButton.addClickListener(l ->{
            try {
                bookDao.deleteBook(book);
            } catch (SQLException e){
                Notification notification = new Notification("Book can`t be deleted");
                notification.setDelayMsec(1000);
                notification.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                notification.show(Page.getCurrent());
            } finally {
                updateBookGrid();
            }
        });

        deleteAuthorButton.addClickListener(l ->{
            try {
                authorDao.deleteAuthor(author);
            } catch (SQLException e){
                Notification notification = new Notification("Author can`t be deleted, he has books");
                notification.setDelayMsec(1000);
                notification.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                notification.show(Page.getCurrent());
            } finally {
                updateAuthorGrid();
            }
        });

        deleteGenreButton.addClickListener(l ->{
            try {
                genreDao.deleteGenre(genre);
            } catch (SQLException e){
                Notification notification = new Notification("Genre can`t be deleted, it has books");
                notification.setDelayMsec(1000);
                notification.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                notification.show(Page.getCurrent());
            } finally {
                updateGenreGrid();
            }
        });

    }
}