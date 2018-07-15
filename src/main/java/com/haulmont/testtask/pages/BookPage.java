package com.haulmont.testtask.pages;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.dao.BookDao;
import com.haulmont.testtask.dao.GenreDao;
import com.haulmont.testtask.dao.impl.AuthorDaoImpl;
import com.haulmont.testtask.dao.impl.BookDaoImpl;
import com.haulmont.testtask.dao.impl.GenreDaoImpl;
import com.haulmont.testtask.entity.Author;
import com.haulmont.testtask.entity.Book;
import com.haulmont.testtask.entity.Genre;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

public class BookPage extends Window {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private TextField name = new TextField("Name");
    private NativeSelect author = new NativeSelect("Author");
    private NativeSelect genre = new NativeSelect("Genre");
    private DateField publicationYear = new DateField("Publication Year");
    private TextField publicationCity = new TextField("Publication City");
    private NativeSelect publisher = new NativeSelect("Publisher");
    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");
    private FieldGroup fieldGroup;
    private boolean isEdit;
    BookDao bookDao;
    AuthorDao athorDao;
    GenreDao genreDao;
    Book book;

    public BookPage (Book initBook){

        bookDao=BookDaoImpl.getInstance();
        athorDao=AuthorDaoImpl.getInstance();
        genreDao=GenreDaoImpl.getInstance();

        center();
        setModal(true);
        setResizable(false);

        name.setNullRepresentation("");
        publicationCity.setNullRepresentation("");
        author.setNullSelectionAllowed(false);
        genre.setNullSelectionAllowed(false);
        publisher.setNullSelectionAllowed(false);
        verticalLayout.addComponents(name,author,genre,publicationYear,publicationCity,publisher);
        author.setContainerDataSource(new BeanItemContainer<>(Author.class, athorDao.getAllAuthors()));
        genre.setContainerDataSource(new BeanItemContainer<>(Genre.class, genreDao.getAllGenres()));
        publisher.addItems(
                Book.Publisher.Moscow,
                Book.Publisher.Piter,
                Book.Publisher.OReilly);
        horizontalLayout.addComponents(okButton, cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        setContent(verticalLayout);

        if (initBook==null){
            setCaption("New Book");
            this.book=new Book();
            isEdit=false;
        } else {
            setCaption("Edit Book");
            this.book=initBook;
            isEdit=true;
        }

        fieldGroup = new BeanFieldGroup<>(Book.class);
        fieldGroup.setItemDataSource(new BeanItem<>(book));
        fieldGroup.bind(name, "name");
        fieldGroup.bind(author, "author");
        fieldGroup.bind(genre, "genre");
        fieldGroup.bind(publicationYear, "publicationDate");
        fieldGroup.bind(publicationCity, "publicationCity");
        fieldGroup.bind(publisher, "publisher");

        name.addValidator(new RegexpValidator("[\\w\\d\\s]+", "Use letters,numbers,spaces"));
        publicationCity.addValidator(new RegexpValidator("[\\w\\d\\s]+", "Use letters,numbers,spaces"));
        publicationYear.addValidator(new NullValidator("Select Date, formate dd.mm.yy", false));
        author.addValidator(new NullValidator("Select Author", false));
        genre.addValidator(new NullValidator("Select Genre", false));
        publisher.addValidator(new NullValidator("Select Publisher", false));

        cancelButton.addClickListener(clickEvent -> close());
        okButton.addClickListener(clickEvent -> {
            try{
                fieldGroup.commit();
                if (isEdit){
                    bookDao.updateBook(book);
                }else{
                    bookDao.createBook(book);
                }
                close();
            }catch (FieldGroup.CommitException e) {
            }
        });

    }


}