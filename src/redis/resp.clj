(ns redis.resp
  (:import [java.io ByteArrayInputStream InputStream]))

(defn ^InputStream mock-response_1 [resp]
  (ByteArrayInputStream. (.getBytes resp)))

(defn get-tokens [^InputStream in]
  (let [character (.read in)]
    (if (= character -1)
      nil
      (cons (char character) (lazy-seq (get-tokens in))))))

(defn first-line [s]
  (take-while #(not= % \newline) s))

(defn rest-lines [s]
  (drop (+ 1 (count (first-line s))) s))

(defn parse-first-line [s] 
  (->> s next (take-while #(not= \return %)) (apply str)))

(defn parse-bulk-string [s]
   (let [num-chars-str (parse-first-line s)
              num-chars (Integer. num-chars-str)
              chars-to-drop (+ (.length num-chars-str) 3)]
          (if (= num-chars -1)
            nil
            (apply str (take num-chars (drop chars-to-drop s))))))

(declare parse-resp)


(defn parse-array [s]
  (let [first-line (Integer. (parse-first-line s))]
    (case first-line
      -1 nil
      0 []
      "NOT IMPLEMENTED")))

(defn parse-resp [tokens]
  (let [first-character (first tokens)]
    (cond 
      (= first-character \+)      (parse-first-line tokens)
      (= first-character \-)      (parse-first-line tokens)
      (= first-character \:)      (Integer. (parse-first-line tokens))
      (= first-character \$)      (parse-bulk-string tokens)
      (= first-character \*)      (parse-array tokens) )))


