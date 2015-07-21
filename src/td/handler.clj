(ns td.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [td.middleware :as logger]
            [stencil.core :refer [render-string]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn read-template [filename]
  (println (route/resources "index.html"))
  (slurp (clojure.java.io/resource filename)))


(defroutes app-routes
           (GET  "/" [] (resource-response "index.html" {:root "public"}))
           (GET  "/widgets" [] (response [{:name "Widget 1"} {:name "Widget 2"}]))
           (GET "/blah" [] (render-string (read-template "public/templates/index.html")))
           (route/resources "/" {:root "public"})
           (route/not-found "Page not found"))
(def app
  (-> app-routes
      (logger/wrap-request-logging)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults site-defaults)))