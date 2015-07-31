(ns td.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [td.middleware :as logger]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clostache.parser :refer [render-resource]]))

(defn read-template [filename]
  (slurp (clojure.java.io/resource "templeates/index.html")))


(defroutes app-routes
           (GET "/blah" []
                (render-resource "templates/index.html" {:name "Kyle"}))
           (route/resources "/" {:root "public"})
           (route/not-found "Page not found"))
(def app
  (-> app-routes
      (logger/wrap-request-logging)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults site-defaults)))