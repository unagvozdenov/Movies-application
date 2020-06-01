(ns movieApp.view
  (:use hiccup.page hiccup.element)
  (:require 
    [hiccup.core :refer [h]]
    [hiccup.form :as form]
    [clojure.string :as str]
    [movieApp.layout :as layout]
    [clj-http.client :as client]
    [clojure.java.io :as io]
    [movieApp.db :as db]
    [ring.util.anti-forgery :as anti-forgery])
    )

(defn convert-answer [answer]
  (if (= answer true) "Yes" "No"))

(defn display-all-movies [movies]
  [:div {:class "text-center px-2"}
   [:h1 "All movies"]
   [:p  {:class "font-style-class"}" *Movie Reviews*" ]
   [:div {:class "img-style"}
   [:img  {:src "../images/movieAppImg.jpg"} ]]

   [:br]
   [:table  {:class "table table-bordered"}
    [:th "Id"]
    [:th "Name"]
    [:th "Rating"]
    [:th "Description"]
    [:th "Genre"]
    [:th "Recommendation"]
    [:th "Trailer"]
    [:th "Remove"]
    [:th "Edit"]
    (map
      (fn [movie]
        [:tr
         [:td (h (:id movie))]
         [:td (h (:name movie))]
         [:td (h (:rating movie))]
         [:td (h (:description movie))]
         [:td (h (:genre movie))]
         [:td (convert-answer (:recommendation movie))]
         [:td [:a {:href (str (:trailer movie))} "Trailer"]]
         [:td [:a {:href (str "/delete/" (h (:id movie)))} "Remove"]]
         [:td [:a {:href (str "/update/" (h (:id movie)))} "Edit"]]
         ]) movies)]])

(defn add-movie-form []
  [:div {:class "form-group card px-2"}
   [:h1 {:class "text-center"} "Add movie"]

   (form/form-to [:post "/create-movie"]
                 (anti-forgery/anti-forgery-field)
                 [:div {:class "form-group"}
                  (form/label "name" "Name")
                  (form/text-field {:class "form-control"} "name")]
                 [:div {:class "form-group"}
                  (form/label "rating" "Rating")
                  (form/drop-down {:class "form-control"} "rating" [1,2,3,4,5])]
                 [:div {:class "form-group"}                  
                  (form/label "description" "Description ")                  
                  (form/text-area {:class "form-control"}  "description")]
                 [:div {:class "form-group"}                  
                  (form/label "genre" "Genre")
                  (form/drop-down {:class "form-control"} "genre" ["Action", "Drama", "Comedy", "Mystery", "Romance", "SCI-FI", "Animation", "Horror"])]
                 [:div {:class "form-group"}
                  (form/label "recommendation" "Recommendation")
                  (form/check-box {:class "form-control" :style "height: 30px; width: 30px"}  "recommendation")]
                  [:div {:class "form-group"}
                   (form/label "trailer" "Link to trailer")
                   (form/text-field {:class "form-control"} "trailer")]
                 (form/submit-button {:onclick "return validate()" :class "btn btn-primary btn-lg btn-block"}  "Add movie")
                 [:br])])

(defn index-page [movies]
  (layout/common ""
                        [:div {:class "col-lg-1"}]
                        [:div {:class "col-lg-10"}
                         (display-all-movies movies)
                         ]
                        [:div {:class "col-lg-1"}]   
                        ))

(defn add-page []
      (layout/common ""
                     [:div {:class "col-lg-1"}]
                     [:div {:class "col-lg-10"}
                      (add-movie-form)]
                     [:div {:class "col-lg-1"}]
                     ))

(defn update-movie-form [movie]
   (layout/common ""
  [:div {:class "form-group card px-2"}
   [:h1 {:class "text-center"} "Edit movie"]
    (map 
      (fn [movie]
     (form/form-to [:post "/update-movie"]
                 (anti-forgery/anti-forgery-field)
                 (form/hidden-field "id" (:id movie))
                 [:div {:class "form-group"}
                  (form/label "name" "Name")
                  (form/text-field {:class "form-control"} "name" (:name movie) )]
                 [:div {:class "form-group"}
                  (form/label "rating" "Rating")
                  (form/drop-down {:class "form-control"} "rating" [1,2,3,4,5] (:rating movie))]
                 [:div {:class "form-group"}
                  (form/label "description" "Description ")
                  (form/text-area {:class "form-control"}  "description" (:description movie))]
                 [:div {:class "form-group"}
                  (form/label "genre" "Genre")
                  (form/drop-down {:class "form-control"} "genre" ["Action", "Drama", "Comedy", "Mystery", "Romance", "SCI-FI", "Animation", "Horror"] (:description movie))]
                 [:div {:class "form-group"}
                  (form/label "recommendation" "Recommendation")
                  (form/check-box {:class "form-control check-box-style" :style "height: 30px; width: 30px"} "recommendation" (:recommendation movie))]
                 [:div {:class "form-group"}
                  (form/label "trailer" "Link to trailer")
                  (form/text-field {:class "form-control"} "trailer" (:trailer movie))]
                 (form/submit-button {:class "btn btn-primary btn-lg btn-block"}  "Edit movie")
                 [:br])) movie)]
    
))


