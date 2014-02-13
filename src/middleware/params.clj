(ns middleware.params)


;AÑADIR PARÁMETROS AL REQUEST
(defn wrap-params [request]
  (let [query-params (request :query-string)
        form-params (request :body)]
    (request)))
