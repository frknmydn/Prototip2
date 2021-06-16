package com.furkanmeydan.prototip2.DataLayer;

import com.furkanmeydan.prototip2.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.furkanmeydan.prototip2.DataLayer.RxTestUtil.setupOfflineTask;
import static com.furkanmeydan.prototip2.DataLayer.RxTestUtil.setupTask;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

public class UserTestFirestore {
    private static final String ANY_USERID = "ANY DATE";
    private static final String ANY_NAME_SURNAME = "ANY DATE";
    private static final String BIRTHDATE = "ANY DATE";
    private static final String ANY_PROFILE_PIC = "ANY DATE";
    private static final String ANY_EMAIL = "ANY DATE";
    private static final String ANY_GENDER = "ANY DATE";
    private static final String ANY_ONESIGNALID = "ANY DATE";


    //String userID,String nameSurname, String birthDate, String profilePicture, String email, String gender, String oneSignalID
    private User childData = new User(ANY_USERID,ANY_NAME_SURNAME,BIRTHDATE, ANY_PROFILE_PIC,ANY_EMAIL, ANY_GENDER, ANY_ONESIGNALID);


    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private DocumentReference emptyDocumentReference;

    @Mock
    private CollectionReference collectionReference;

    @Mock
    private CollectionReference emptyCollectionReference;

    @Mock
    private Query queryReference;

    @Mock
    private Query emptyQueryReference;

    @Mock
    private DocumentSnapshot documentSnapshot;

    @Mock
    private DocumentSnapshot emptyDocumentSnapshot;

    @Mock
    private QuerySnapshot querySnapshot;
    @Mock
    private QuerySnapshot emptyQuerySnapshot;

    @Mock
    private Task<DocumentSnapshot> emptyDocumentSnapshotTask;

    @Mock
    private Task<DocumentSnapshot> documentSnapshotTask;

    @Mock
    private Task<DocumentReference> documentRefTask;

    @Mock
    private Task<QuerySnapshot> queryResultTask;

    @Mock
    private Task<QuerySnapshot> emptyQueryResultTask;

    @Mock
    private ListenerRegistration registration;





    @Mock
    Task<DocumentSnapshot> ds;

    private ArrayList<User> childDataList = new ArrayList<>();


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        setupTask(documentSnapshotTask);
        setupTask(emptyDocumentSnapshotTask);
        setupTask(queryResultTask);
        setupTask(emptyQueryResultTask);
        setupTask(mockVoidTask);
        setupOfflineTask(documentReference, registration);

        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(emptyDocumentReference.get()).thenReturn(emptyDocumentSnapshotTask);
        when(collectionReference.get()).thenReturn(queryResultTask);
        when(emptyCollectionReference.get()).thenReturn(emptyQueryResultTask);
        when(queryReference.get()).thenReturn(queryResultTask);
        when(emptyQueryReference.get()).thenReturn(emptyQueryResultTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);
        when(documentReference.delete()).thenReturn(mockVoidTask);
        //when(documentReference.update(updateMap)).thenReturn(mockVoidTask);
        //when(collectionReference.add(setData)).thenReturn(documentRefTask);
        when(documentSnapshot.toObject(User.class)).thenReturn(childData);
        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
        when(emptyDocumentSnapshot.exists()).thenReturn(false); //This snapshots exist
        when(querySnapshot.isEmpty()).thenReturn(false);
        when(querySnapshot.toObjects(User.class)).thenReturn(childDataList);
        ds = documentReference.get();
        documentSnapshotTask = documentReference.get();

    }

    @Test
    public void getUserProfileDocNotNull() {
        assertThat(documentSnapshotTask.getResult()).isNotNull();
    }

    @Test
    public void getUserProfileIsSuccessfull(){
        assertThat(documentSnapshotTask.isSuccessful());
    }

    @Test
    public void getUserProfileToObjectNotNull(){
        assertThat(Objects.requireNonNull(documentSnapshotTask.getResult()).toObject(User.class)).isNotNull();
    }

    @Test
    public void getUserProfileInstanceOfUserClass() {
        assertThat(documentSnapshotTask.getResult().toObject(User.class)).isInstanceOf(User.class);
    }

    @Test
    public void getUserProfileFieldBirthDateNotEmpty(){
        assertThat(documentSnapshotTask.getResult().toObject(User.class).getBirthDate()).isNotNull();
    }

    @Test
    public void getUserProfileFieldNameSurnameNotEmpty(){
        assertThat(documentSnapshotTask.getResult().toObject(User.class).getNameSurname()).isNotNull();
    }

    @Test
    public void getUserProfileFieldEmailNotEmpty(){
        assertThat(documentSnapshotTask.getResult().toObject(User.class).getEmail()).isNotNull();
    }

    @Test
    public void getUserProfileFieldGenderNotEmpty(){

        assertThat(documentSnapshotTask.getResult().toObject(User.class).getGender()).isNotNull();
    }

    @Test
    public void getUserProfileFieldProfilePictureNotEmpty(){
        assertThat(documentSnapshotTask.getResult().toObject(User.class).getProfilePicture()).isNotNull();
    }


    @Test
    public void nameSurnameIsValid(){

        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
        System.out.println(pattern.matcher(documentSnapshotTask.getResult().toObject(User.class).getNameSurname()).find());


        if(pattern.matcher(documentSnapshotTask.getResult().toObject(User.class).getNameSurname()).find()){
            System.out.println(documentSnapshotTask.getResult().toObject(User.class).getNameSurname().trim().replaceAll("[^A-Za-z\\s]","").replaceAll(" +"," "));
        }

        assertThat(documentSnapshotTask.getResult().toObject(User.class).getNameSurname().contains(""));


    }

    @Test
    public void emailContainsParameter(){
        //Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");


        String email = documentSnapshotTask.getResult().toObject(User.class).getEmail();
        EmailValidator validator = EmailValidator.getInstance();
        System.out.println(!validator.isValid(email));
        assertThat(validator.isValid(email)).isTrue();

    }
}