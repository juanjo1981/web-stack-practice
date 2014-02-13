;----------------FAKE TESTING ------------------

(defn index []
  "this is the index handler")
(defn foo []
  "this is the foo handler")

(defn bar []
  "this is the bar handler")

(defn juanjo []
  "this is the juanjo handler")

(def routes {"" index, "/foo" foo, "/bar" bar, "/juanjo" juanjo})


;---------------END FAKE TESTING ---------