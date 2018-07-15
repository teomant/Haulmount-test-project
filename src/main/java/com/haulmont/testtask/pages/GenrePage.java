package com.haulmont.testtask.pages;

import com.haulmont.testtask.dao.GenreDao;
import com.haulmont.testtask.dao.impl.GenreDaoImpl;
import com.haulmont.testtask.entity.Genre;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

public class GenrePage extends Window {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private TextField genreTextField = new TextField("Genre name");
    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");
    private FieldGroup fieldGroup;
    private boolean isEdit;
    GenreDao genreDao;
    Genre genre;

    public GenrePage (Genre initGenre){

        genreDao=GenreDaoImpl.getInstance();

        center();
        setModal(true);
        setResizable(false);

        genreTextField.setNullRepresentation("");
        verticalLayout.addComponent(genreTextField);
        horizontalLayout.addComponents(okButton, cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        setContent(verticalLayout);

        if (initGenre==null){
            setCaption("New Genre");
            this.genre=new Genre();
            isEdit=false;
        } else {
            setCaption("Edit Genre");
            this.genre=initGenre;
            isEdit=true;
        }

        fieldGroup = new BeanFieldGroup<>(Genre.class);
        fieldGroup.setItemDataSource(new BeanItem<>(genre));
        fieldGroup.bind(genreTextField, "name");

        genreTextField.addValidator(new RegexpValidator("[\\w\\s-]+", "Use letters"));

        cancelButton.addClickListener(clickEvent -> close());
        okButton.addClickListener(clickEvent -> {
            try{
                fieldGroup.commit();
                if (isEdit){
                    genreDao.updateGenre(genre);
                }else{
                    genreDao.createGenre(genre);
                }
                close();
            }catch (FieldGroup.CommitException e) {
            }
        });

    }


}
