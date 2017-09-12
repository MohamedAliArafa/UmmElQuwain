package com.a700apps.ummelquwain.service;

import com.a700apps.ummelquwain.models.User;
import com.a700apps.ummelquwain.models.request.AlbumContentRequestModel;
import com.a700apps.ummelquwain.models.request.EventRequestModel;
import com.a700apps.ummelquwain.models.request.FavouriteRequestModel;
import com.a700apps.ummelquwain.models.request.JoinUsRequestModel;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.request.NewsDetailsRequestModel;
import com.a700apps.ummelquwain.models.request.ProgramDetailsRequestModel;
import com.a700apps.ummelquwain.models.request.SearchRequestModel;
import com.a700apps.ummelquwain.models.request.StationDetailsRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsModel;
import com.a700apps.ummelquwain.models.response.Albums.AlbumModel;
import com.a700apps.ummelquwain.models.response.Albums.AlbumsModel;
import com.a700apps.ummelquwain.models.response.ContactUs.ContactUsModel;
import com.a700apps.ummelquwain.models.response.Events.EventModel;
import com.a700apps.ummelquwain.models.response.Events.EventsModel;
import com.a700apps.ummelquwain.models.response.Message.MessageModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarDetailModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarModel;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorModel;
import com.a700apps.ummelquwain.models.response.Station.StationModel;
import com.a700apps.ummelquwain.models.response.Station.StationsModel;
import com.a700apps.ummelquwain.models.response.program.ProgramModel;
import com.a700apps.ummelquwain.models.response.program.ProgramsModel;

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
