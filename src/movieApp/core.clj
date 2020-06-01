(ns movieApp.core
  (:use compojure.core
        ring.util.json-response 
        ring.adapter.jetty
 hiccup.page hiccup.element)

  (:require [compojure.core :refer [defroutes GET POST]]
            [clostache.parser :as clostache]
            [clojure.string :as str]     
            [ring.util.response :as ring]
            [clj-http.client :as client]
            [clojure.java.io :as io]
            [hiccup.element :as element]
            [movieApp.view :as view]
            [movieApp.db :as db]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            ))

(defn display-all-movies []
  (view/index-page (db/get-all-movies)))

(defn create-movie [name year description genre recommendation image trailer]
  (when-not (str/blank? name)
    (db/create-movie name year description genre recommendation image trailer))
      (view/index-page (db/get-all-movies)))

(defn delete-movie [id]
  (when-not (str/blank? id)
    (db/delete-movie id))
  (ring/redirect "/"))


(defn show-update-view [id]
 (view/update-movie-form (db/get-movie-by-id id)))

(defn show-create-view []
(view/add-page))

(defn update-movie [id name rating description genre recommendation image trailerLink]
    (when-not (str/blank? id)
    (db/update-movie id name rating description genre recommendation image trailerLink))
   (view/index-page (db/get-all-movies)))

(defroutes my_routes
  (GET "/" [] (display-all-movies))
  (POST "/create-movie" [name rating description genre recommendation image trailer] (create-movie name rating description genre recommendation image trailer))
  (POST "/update-movie"  [id name rating description genre recommendation image trailer] (update-movie id name rating description genre recommendation image trailer))
  (GET "/update/:id" [id] (show-update-view id))
  (GET "/create" [] (show-create-view))
  (GET "/delete/:id" [id]  (delete-movie id))
  (GET "/api/all" [] (json-response (db/get-all-movies))))

(def app
  (wrap-defaults my_routes site-defaults))
