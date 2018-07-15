package com.haulmont.testtask.pages;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.dao.impl.AuthorDaoImpl;
import com.haulmont.testtask.entity.Author;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

public class AuthorPage extends Window {

    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private TextField firstName = new TextField("Firstname");
    private TextField surName = new TextField("Surname");
    private TextField lastName = new TextField("Lastname");
    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");
    private FieldGroup fieldGroup;
    private boolean isEdit;
    AuthorDao authorDao;
    Author author;

    public AuthorPage (Author initAuthor){

        authorDao=AuthorDaoImpl.getInstance();

        center();
        setModal(true);
        setResizable(false);

        firstName.setNullRepresentation("");
        surName.setNullRepresentation("");
        lastName.setNullRepresentation("");
        verticalLayout.addComponents(firstName,surName,lastName);
        horizontalLayout.addComponents(okButton, cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        setContent(verticalLayout);

        if (initAuthor==null){
            setCaption("New Author");
            this.author=new Author();
            isEdit=false;
        } else {
            setCaption("Edit Author");
            this.author=initAuthor;
            isEdit=true;
        }

        fieldGroup = new BeanFieldGroup<>(Author.class);
        fieldGroup.setItemDataSource(new BeanItem<>(author));
        fieldGroup.bind(firstName, "firstname");
        fieldGroup.bind(surName, "surname");
        fieldGroup.bind(lastName, "lastname");

        firstName.addValidator(new RegexpValidator("\\w+", "Use letters"));
        surName.addValidator(new RegexpValidator("\\w+", "Use letters"));
        lastName.addValidator(new RegexpValidator("\\w+", "Use letters"));

        cancelButton.addClickListener(clickEvent -> close());
        okButton.addClickListener(clickEvent -> {
            try{
                fieldGroup.commit();
                if (isEdit){
                    authorDao.updateAuthor(author);
                }else{
                    authorDao.createAuthor(author);
                }
                close();
            }catch (FieldGroup.CommitException e) {
            }
        });

    }


}
