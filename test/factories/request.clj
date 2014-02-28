(ns factories.request)

(defn request 
	([method uri]
		(request method uri nil nil))
	([method uri params]
		(request method uri params nil))
	([method uri params header]

	{:server-port       (.getServerPort request)
    :server-name        (.getServerName request)
    :remote-addr        (.getRemoteAddr request)
    :uri                (.getRequestURI request)
    :query-string       (.getQueryString request)
    :scheme             (keyword (.getScheme request))
    :request-method    (-> request .getMethod .toString .toLowerCase) 
    :headers            (get-headers request)
    :content-type       (.getContentType request)
    :content-length     (get-content-length request)
    :character-encoding (.getCharacterEncoding request)
    ;:ssl-client-cert    (get-client-cert request)
    :body               (.getInputStream request)}
 
    ))


 ; {:server-port        (.getServerPort request)
 ;    :server-name        (.getServerName request)
 ;    :remote-addr        (.getRemoteAddr request)
 ;    :uri                (.getRequestURI request)
 ;    :query-string       (.getQueryString request)
 ;    :scheme             (keyword (.getScheme request))
 ;    :request-method    (-> request .getMethod .toString .toLowerCase) 
 ;    :headers            (get-headers request)
 ;    :content-type       (.getContentType request)
 ;    :content-length     (get-content-length request)
 ;    :character-encoding (.getCharacterEncoding request)
 ;    ;:ssl-client-cert    (get-client-cert request)
 ;    :body               (.getInputStream request)}