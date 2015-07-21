(ns td.middleware
  (:require
    [taoensso.timbre :as timbre
     :refer (log  trace  debug  info  warn  error  fatal  report
                  logf tracef debugf infof warnf errorf fatalf reportf
                  spy get-env log-env)]))

(defn- printLog [msg & vals]
  (let [line (apply format msg vals)]
    (locking System/out (debug line))))

(defn wrap-request-logging [handler]
  (fn [{:keys [request-method uri] :as req}]
    (let [start  (System/currentTimeMillis)
          resp   (handler req)
          finish (System/currentTimeMillis)
          total  (- finish start)]
      (printLog "request %s %s (%dms)" request-method uri total)
      resp)))