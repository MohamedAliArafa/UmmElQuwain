package com.a700apps.ummelquwain.service;

import com.a700apps.ummelquwain.models.request.AlbumContentRequestModel;
import com.a700apps.ummelquwain.models.request.LanguageRequestModel;
import com.a700apps.ummelquwain.models.request.NewsDetailsRequestModel;
import com.a700apps.ummelquwain.models.request.StationDetailsRequestModel;
import com.a700apps.ummelquwain.models.request.StationsRequestModel;
import com.a700apps.ummelquwain.models.response.AboutUs.AboutUsModel;
import com.a700apps.ummelquwain.models.response.Albums.AlbumModel;
import com.a700apps.ummelquwain.models.response.Albums.AlbumsModel;
import com.a700apps.ummelquwain.models.response.ContactUs.ContactUsModel;
import com.a700apps.ummelquwain.models.response.Events.EventDetailModel;
import com.a700apps.ummelquwain.models.response.Events.EventsModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarDetailModel;
import com.a700apps.ummelquwain.models.response.NewsBar.NewsBarModel;
import com.a700apps.ummelquwain.models.response.Sponsors.SponsorModel;
import com.a700apps.ummelquwain.models.response.Station.StationModel;
import com.a700apps.ummelquwain.models.response.Station.StationsModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mohamed.arafa on 8/27/2017.
 */

public interface ApiService {

    @POST("Sponsers/GetAllSponsers")
    Call<SponsorModel> getAllSponsors(@Body LanguageRequestModel language);

    //News
    @POST("News/GetAllNews")
    Call<NewsBarModel> getAllNews(@Body LanguageRequestModel language);

    @POST("News/NewsBar")
    Call<NewsBarModel> getNewsBar(@Body LanguageRequestModel language);

    @POST("News/NewsDetails")
    Call<NewsBarDetailModel> getNewsDetails(@Body NewsDetailsRequestModel newsModel);

    //Events
    @POST("Events/GetAllEvents")
    Call<EventsModel> getAllEvents(@Body LanguageRequestModel language);

    @POST("Events/EventDetails")
    Call<EventDetailModel> getEventDetails(@Body LanguageRequestModel language);

    //Albums
    @POST("Albums/GetAll")
    Call<AlbumsModel> getAllAlbums(@Body LanguageRequestModel language);

    @POST("Albums/AlbumContent")
    Call<AlbumModel> getAlbumContent(@Body AlbumContentRequestModel newsModel);

    //Content
    @POST("Content/AboutUs")
    Call<AboutUsModel> getAboutUs(@Body LanguageRequestModel language);

    @POST("Content/ContactUs")
    Call<ContactUsModel> getContactUs(@Body LanguageRequestModel language);

    //Station
    @POST("Stations/GetAllStations")
    Call<StationsModel> getAllStations(@Body StationsRequestModel language);

    @POST("Stations/StationsDetails")
    Call<StationModel> getStationDetails(@Body StationDetailsRequestModel newsModel);

}
