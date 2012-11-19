(ns clj-fo.test.helpers
  (:require [clojure.string :as str]))

(defn random-string
  "generates random string of 'length' characters"
  [length]
  (let [upper        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        lower        (str/lower-case upper)
        numbers      "1234567890"
        alphanumeric (str upper lower numbers)]
    (loop [acc []]
      (if (= (count acc) length) (apply str acc)
          (recur (conj acc (rand-nth alphanumeric)))))))

(defn temp-pdf-filename
  "Creates a temporary pdf filename"
  []
  (str "/tmp/" (random-string 40) ".pdf"))
