package com.ubn.ummelquwain.service;

import com.ubn.ummelquwain.models.User;
import com.ubn.ummelquwain.models.request.AlbumContentRequestModel;
import com.ubn.ummelquwain.models.request.CommentRequestModel;
import com.ubn.ummelquwain.models.request.EventRequestModel;
import com.ubn.ummelquwain.models.request.FavouriteRequestModel;
import com.ubn.ummelquwain.models.request.JoinUsRequestModel;
import com.ubn.ummelquwain.models.request.LanguageRequestModel;
import com.ubn.ummelquwain.models.request.NewsDetailsRequestModel;
import com.ubn.ummelquwain.models.request.ProgramDetailsRequestModel;
import com.ubn.ummelquwain.models.request.SearchRequestModel;
import com.ubn.ummelquwain.models.request.StationDetailsRequestModel;
import com.ubn.ummelquwain.models.request.StationsRequestModel;
import com.ubn.ummelquwain.models.response.AboutUs.AboutUsModel;
import com.ubn.ummelquwain.models.response.Albums.AlbumModel;
import com.ubn.ummelquwain.models.response.Albums.AlbumsModel;
import com.ubn.ummelquwain.models.response.ContactUs.ContactUsModel;
import com.ubn.ummelquwain.models.response.Events.EventModel;
import com.ubn.ummelquwain.models.response.Events.EventsModel;
import com.ubn.ummelquwain.models.response.Message.MessageModel;
import com.ubn.ummelquwain.models.response.NewsBar.NewsBarDetailModel;
import com.ubn.ummelquwain.models.response.NewsBar.NewsBarModel;
import com.ubn.ummelquwain.models.response.Sponsors.SponsorModel;
import com.ubn.ummelquwain.models.response.Station.StationModel;
import com.ubn.ummelquwain.models.response.Station.StationsModel;
import com.ubn.ummelquwain.models.response.program.ProgramModel;
import com.ubn.ummelquwain.models.response.program.ProgramsModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public interface ApiService {

    //Sponsors
    @POST("Sponsers/GetAllSponsers")
    Call<SponsorModel> getAllSponsors(@Body LanguageRequestModel language);

    //News
    @POST("News/GetAllNews")
    Call<NewsBarModel> getAllNews(@Body LanguageRequestModel language);

    @POST("News/NewsDetails")
    Call<NewsBarDetailModel> getNewsDetails(@Body NewsDetailsRequestModel newsModel);

    //Events
    @POST("Events/GetAllEvents")
    Call<EventsModel> getAllEvents(@Body LanguageRequestModel language);

    @POST("Events/EventDetails")
    Call<EventModel> getEventDetails(@Body EventRequestModel language);

    //Albums
    @POST("Albums/GetAll")
    Call<AlbumsModel> getAllAlbums(@Body LanguageRequestModel language);

    @POST("Albums/AlbumContent")
    Call<AlbumModel> getAlbumContent(@Body AlbumContentRequestModel newsModel);

    @POST("Albums/Search")
    Call<AlbumsModel> searchAlbums(@Body SearchRequestModel model);

    //Content
    @POST("Content/AboutUs")
    Call<AboutUsModel> getAboutUs(@Body LanguageRequestModel language);

    @POST("Content/ContactUs")
    Call<ContactUsModel> getContactUs(@Body LanguageRequestModel language);

    //Station
    @POST("Stations/GetAllStations")
    Call<StationsModel> getAllStations(@Body StationsRequestModel language);

    @POST("Stations/StationsDetails")
    Call<StationModel> getStationDetails(@Body StationDetailsRequestModel model);

    @POST("Stations/Search")
    Call<StationsModel> searchStations(@Body SearchRequestModel model);

    //Program
    @POST("Programs/GetAllPrograms")
    Call<ProgramsModel> getAllPrograms(@Body StationsRequestModel model);

    @POST("Programs/ProgramDetails")
    Call<ProgramModel> getProgramDetails(@Body ProgramDetailsRequestModel model);

    @POST("Programs/Search")
    Call<ProgramsModel> searchPrograms(@Body SearchRequestModel model);

    @POST("Programs/AddComment")
    Call<MessageModel> addComment(@Body CommentRequestModel model);

    //JoinUs
    @Multipart
    @POST("Upload/Post")
    Call<MessageModel> addRecord(@Part MultipartBody.Part file);

    //User
    @POST("Users/Register_Login")
    Call<MessageModel> loginUser(@Body User user);

    //Fav
    @POST("Favourite/AddToFav")
    Call<MessageModel> addTofav(@Body FavouriteRequestModel model);

    @POST("Favourite/RemoveFromFav")
    Call<MessageModel> removeFromfav(@Body FavouriteRequestModel model);

    @POST("Favourite/GetAll")
    Call<StationsModel> getFavStations(@Body StationsRequestModel model);

    //UserRequests
    @POST("UserRequests/JoinsUs")
    Call<MessageModel> joinUs(@Body JoinUsRequestModel model);
}
