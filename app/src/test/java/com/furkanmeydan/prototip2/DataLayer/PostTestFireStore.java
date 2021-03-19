package com.furkanmeydan.prototip2.DataLayer;

import android.location.Address;
import android.location.Geocoder;

import com.furkanmeydan.prototip2.Model.Post;
import com.furkanmeydan.prototip2.View.UploadPostActivity.UploadPostActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


import static com.furkanmeydan.prototip2.DataLayer.RxTestUtil.setupOfflineTask;
import static com.furkanmeydan.prototip2.DataLayer.RxTestUtil.setupTask;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

public class PostTestFireStore {

    public static final String ANY_DESTINATION = "ANY_DESTINATION";
    public static final String ANY_CITY = "ANY_CITY";
    public static final String ANY_TIMESTAMP = "1615695028";
    public static final String ANY_PASSENGER_COUNT = "ANY_PASSENGER_COUNT";
    public static final String ANY_DESCRIPTION = "ANY DESCRIPTION";
    public static final String ANY_CAR_DETAILS ="Araç Siyah Mercedes A180";
    public static final String ANY_OWNER_ID ="ANY_OWNER_ID";
    public static final String ANY_FROM_LAT = "ANY_FROM_LAT";
    public static final String ANY_FROM_LNG = "ANY_FROM_LNG";
    public static final String ANY_TO_LAT = "ANY_TO_LAT";
    public static final String ANY_TO_LNG = "ANY_TO_LNG";
    public static final String ANY_DROPDOWN_CITY = "Aydın";
    public static final String ANY_SHAREDPREF_CITY = "İzmir";
    //public static final String

    public static final Double ANY_DOUBLE_LAT = 37.85819170277286;
    public static final Double ANY_DOUBLE_LNG = 27.857791632413864;
    public static final Double ANY_DOUBLE_LAT2 = 34.98473993720123;
    public static final Double ANY_DOUBLE_LNG2= 23.857781632413864;

    //Post mockPost = new Post(ANY_CITY,ANY_OWNER_ID,ANY_PASSENGER_COUNT,ANY_DESTINATION,ANY_DESCRIPTION,ANY_TIMESTAMP,ANY_CAR_DETAILS,ANY_TO_LAT,ANY_TO_LNG,ANY_FROM_LAT,ANY_FROM_LNG,1,"Erkek",1);

    /*
    public Post(String city, int passengerCount,
                String destination, String description, long timestamp, String carDetail,
                Double toLat, Double toLng, Double fromLat, Double fromLng, int status, String userGender, int direction)

*/
    Post mockPost = new Post(ANY_CITY,1,ANY_DESTINATION,ANY_DESCRIPTION,100000,ANY_CAR_DETAILS,100.213,231.312,3123.123,3213.421,1,"ERKEK",1);

    private Timestamp mockServerTimestamp = Timestamp.now();

    @Mock
    private Address mockAddress = new Address(Locale.getDefault());

    @Mock
    private Address mockAddress2 = new Address(Locale.getDefault());

    @Mock
    private UploadPostActivity mockPostActivity;

    @Mock
    private Geocoder geocoder = new Geocoder(mockPostActivity, Locale.getDefault());

    @Mock
    private List<Address> mockAddressList2;

    @Mock
    private List<Address> mockAddressList;

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
    Task<DocumentSnapshot> ds;

    @Mock
    private ListenerRegistration registration;


    //Birden fazla post döndürülürdüğü zaman için
    private ArrayList<Post> childDataList = new ArrayList<>();


    @Before
    public void setup() throws IOException {

        MockitoAnnotations.initMocks(this);

        setupTask(documentSnapshotTask);
        setupTask(emptyDocumentSnapshotTask);
        setupTask(queryResultTask);
        setupTask(emptyQueryResultTask);
        setupTask(mockVoidTask);
        setupOfflineTask(documentReference, registration);



        when(mockAddressList.get(0)).thenReturn(mockAddress);
        when(mockAddressList2.get(0)).thenReturn(mockAddress2);
        when(mockAddress.getAdminArea()).thenReturn(ANY_DROPDOWN_CITY);
        when(mockAddress2.getAdminArea()).thenReturn(ANY_SHAREDPREF_CITY);


        when(geocoder.getFromLocation(ANY_DOUBLE_LAT,ANY_DOUBLE_LNG,1)).thenReturn(mockAddressList);
        when(geocoder.getFromLocation(ANY_DOUBLE_LAT2,ANY_DOUBLE_LNG2,1)).thenReturn(mockAddressList2);



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
        when(documentSnapshot.toObject(Post.class)).thenReturn(mockPost);
        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
        when(documentSnapshot.exists()).thenReturn(true); //This snapshots exist
        when(emptyDocumentSnapshot.exists()).thenReturn(false); //This snapshots exist
        when(querySnapshot.isEmpty()).thenReturn(false);
        when(querySnapshot.toObjects(Post.class)).thenReturn(childDataList);
        ds = documentReference.get();
        documentSnapshotTask = documentReference.get();

    }




    @Test
    public void getPostDocumentNotNull(){
        assertThat(documentSnapshotTask.getResult()).isNotNull();
    }

    @Test
    public void getPostDocumentIsSuccessfull(){
        assertThat(documentSnapshotTask.isSuccessful());
    }

    @Test
    public void getPostDocumentObjectNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class)).isNotNull();
    }

    @Test
    public void getPostDocumentObjectInstanceOfPost(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class)).isInstanceOf(Post.class);
    }

    @Test
    public void getPostCityNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getCity()).isNotNull();
    }

    /*
    @Test
    public void getPostOwnerIdNotNull(){
        assertThat(mockPost.getOwnerId()).isNotNull();
    }

     */
    @Test
    public void getPostTimeStampIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getTimestamp()).isNotNull();
    }
    @Test
    public void getPostDescriptionIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getDescription()).isNotNull();
    }
    @Test
    public void getPostDestinationIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getDestination()).isNotNull();

    }
    @Test
    public void getPostPassengerCountIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getPassengerCount()).isNotNull();
    }

    @Test
    public void getPostCarDetailIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getCarDetail()).isNotNull();
    }

    @Test
    public void getPostFromLatIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getFromLat()).isNotNull();
    }
    @Test
    public void getPostFromLngIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getFromLng()).isNotNull();
    }
    @Test
    public void getPostToLatIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getToLat()).isNotNull();
    }
    @Test
    public void getPostToLngIsNotNull(){
        assertThat(documentSnapshotTask.getResult().toObject(Post.class).getToLng()).isNotNull();
    }


    @Test
    public void mapCityControlIsEqual(){
        assertThat(mockAddressList.get(0).getAdminArea().equals(ANY_DROPDOWN_CITY)).isTrue();

    }

    @Test
    public void mapCity() throws IOException {
        assertThat(geocoder.getFromLocation(ANY_DOUBLE_LAT2,ANY_DOUBLE_LNG2,1).get(0).getAdminArea().equals(ANY_SHAREDPREF_CITY)).isTrue();
    }

    @Test
    public void postCarDetailLongerThan15(){

        int test = documentSnapshotTask.getResult().toObject(Post.class).getCarDetail().length();
        System.out.println(test);
        assertThat(test).isGreaterThan(15);

    }

    @Test
    public void postCarDetailRegexIsTrue(){
        String carDet = documentSnapshotTask.getResult().toObject(Post.class).getCarDetail();
        Pattern pattern = Pattern.compile("[^A-Za-z0-9\\sğĞüÜşŞiİöÖçÇ.:,;()']");
        System.out.println(pattern.matcher(carDet).find());
        assertThat(pattern.matcher(carDet).find()).isFalse();
    }

    @Test
    public void postDescriptionRegexIsTrue(){
        String description = documentSnapshotTask.getResult().toObject(Post.class).getDescription();
        Pattern pattern = Pattern.compile("[^A-Za-z0-9\\sğĞüÜşŞiİöÖçÇ.:,;()']");
        System.out.println(pattern.matcher(description).find());
        assertThat(pattern.matcher(description).find()).isFalse();
    }

    /*
    @Test
    public void postTimeStampIsNotValid(){
        String postTimeStamp = documentSnapshotTask.getResult().toObject(Post.class).getTimestamp();
        System.out.println(mockServerTimestamp.getSeconds());
        System.out.println(postTimeStamp);

        Long stringToLong = Long.parseLong(postTimeStamp);
        System.out.println(stringToLong);

        assertThat(stringToLong).isGreaterThan(mockServerTimestamp.getSeconds());

    }

     */

    /*
    @Test
    public void postTimeStampIsNotInFarFuture(){
        //1615852800 14 gün sonrası
        //1614643200
        //1209600 = 14 gün timestamp'i

        String postTimeStamp = documentSnapshotTask.getResult().toObject(Post.class).getTimestamp();


        Long stringToLongFromPost = Long.parseLong(postTimeStamp);
        Long timestamp14days = 1209600L;
        Long serverTimestamp = mockServerTimestamp.getSeconds();

        System.out.println(stringToLongFromPost);
        System.out.println(timestamp14days);
        System.out.println(serverTimestamp);

        Long result = serverTimestamp + timestamp14days - stringToLongFromPost;

        System.out.println("Result: " + result);

        assertThat(result).isGreaterThan(0);

    }

     */







    @Test
    public void onPostAdded() {

    }

    @Test
    public void getPost() {
    }

    @Test
    public void onPostDeleted() {
    }
}