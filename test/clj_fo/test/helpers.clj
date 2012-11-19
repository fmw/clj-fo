;; test/clj_fo/test/helpers.clj: utility functions for tests
;; Copyright 2012, Vixu.com, F.M. (Filip) de Waard <fmw@vixu.com>.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;; http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

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