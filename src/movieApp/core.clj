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

(defn create-movie [name year description genre recommendation image]
  (when-not (str/blank? name)
    (db/create-movie name year description genre recommendation image))
  (ring/redirect "/"))

(defn delete-movie [id]
  (when-not (str/blank? id)
    (db/delete-movie id))
  (ring/redirect "/"))

(def test-file
  (client/get "http://placehold.it/350x150" {:as :byte-array}))

(defn download-image []
   (with-open [w (clojure.java.io/output-stream "test-file.gif")]
     (.write w (:body test-file))))

(defn copy-uri-to-file [uri]
 (with-open [bodystream (:body (clj-http.client/get uri {:as :stream}))]
     (clojure.java.io/copy bodystream (clojure.java.io/file "google_favicon.ico"))))

(defn show-update-view [id]
 (view/update-movie-form (db/get-movie-by-id id)))

(defn update-movie [id name rating description genre recommendation image]
    (when-not (str/blank? id)
    (db/update-movie id name rating description genre recommendation image))
   (view/index-page (db/get-all-movies)))

(defroutes my_routes
  (GET "/" [] (display-all-movies))
  (POST "/" [name rating description genre recommendation image] (create-movie name rating description genre recommendation image))
  (POST "/update-movie"  [id name rating description genre recommendation image] (update-movie id name rating description genre recommendation image))
  (GET "/update/:id" [id] (show-update-view id))
  (GET "/delete/:id" [id]  (delete-movie id))
  (GET "/download" [uri]  (copy-uri-to-file "http://google.com/favicon.ico"))
  (GET "/api/all" [] (json-response (db/get-all-movies))))

(def app
  (wrap-defaults my_routes site-defaults))
