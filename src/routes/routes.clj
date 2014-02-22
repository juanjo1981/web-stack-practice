(ns routes.routes)

(def routes {"/" index, "/foo" foo, "/bar" bar, "/juanjo" juanjo})

(def routes
  
  
 {"/" {:method "get", :params [id]}, "/" {:method "post", :params [id]}}
  )

(defn select-handler [routes])

