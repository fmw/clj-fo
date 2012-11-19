========================================================
clj-fo: Apache FOP wrapper in Clojure for PDF generation
========================================================

The 'clj-fo' library is a tiny Clojure wrapper around `Apache FOP` and
meant for generating PDF documents. It creates XSL-FO output, which is
fed to Apache FOP for processing. It also provides an easy way to
convert existing XSL-FO documents to PDF.

I wrote the library to create PDF invoices for the
`website-management software`_ developed my startup `Vixu.com`_. It
was written to scratch my own itch and is provided as-is, warts and
all. Don't hesitate to contact the author at fmw@vixu.com if you have
any questions, however. I'm happy to help.

You can add clj-fo as a dependency in your Leiningen project.clj
through Clojars:

    [clj-fo "0.0.1"]

The example code also requires clj-time:

    [clj-time "0.4.4"]

Example usage:

.. code:: clojure

    (require '[clj-time.format :as time-format]
             '[clj-time.core :as time-core]
             '[clj-fo.fo :as fo])

    (defn create-invoice
      [invoice-number
       nodes
       & {:keys [company-name name street postal-code city country]}]
      (fo/document
       {:header-blocks (map #(fo/block {:text-align "end"} %)
                            ["Telephone: +31 (0)6 48012240"
                             "Street: IJskelderstraat 30"
                             "Postal code: 5046 NK"
                             "City: Tilburg"
                             "Chamber of Commerce: 18068751"
                             "VAT: 1903.14.849.B01"
                             "Bank: Rabobank 3285.04.165"])}
       (fo/block {:font-size "35pt"
                  :space-before.optimum "0pt"
                  :space-after.optimum "15pt"}
                 "Vixu.com")
       (fo/block {:space-before.optimum "100pt"
                  :space-after.optimum "20pt"}
                 (map fo/block [company-name
                                name
                                street
                                (str postal-code " " city)
                                country]))
       (fo/block {:font-weight "bold"
                  :font-size "16"
                  :space-after.optimum "20pt"}
                 "Invoice")
       (fo/table {:border-width "0.5pt" :space-after.optimum "30pt"}
                 ["4cm" "5cm"]
                 [["Date:" (time-format/unparse
                            (time-format/formatters :date)
                            (time-core/now))]
                  ["Invoice number:" invoice-number]])
       (let [subtotal (apply +
                             (map (fn [{:keys [type full-amount discount]}]
                                    (if (= type :+)
                                      full-amount
                                      (* -1 discount full-amount)))  
                                  nodes))
             vat (* 0.21 subtotal)
             total (+ subtotal vat)]
         (fo/table {:border-width "0.5pt"}
                   ["14cm" "3cm"]
                   (concat
                    (map (fn [{:keys [type description full-amount discount]}]
                           [(str description ":")
                            (fo/block {:text-align "right"}
                                      (format "€ %.2f"
                                              (if (= type :+)
                                                full-amount
                                                (* -1 discount full-amount))))])
                         nodes)
                    [["Subtotal:"
                      (fo/block {:text-align "right"}
                                (format "€ %.2f" subtotal))]
                     ["Value Added Tax (21%):"
                      (fo/block {:text-align "right"}
                                (format "€ %.2f" vat))]
                     [{:font-weight "bold"}
                      "Total:"
                      (fo/block {:text-align "right"}
                                (format "€ %.2f" total))]])))
       (fo/block {:space-before.optimum "35pt"}
                 (str "You are kindly requested to pay within 7 days. "
                      "Please wire the amount due to Rabobank account "
                      "number 3285.04.165."))))
    
    (fo/write-pdf!
     (create-invoice
      "ACME02"
      [{:type :+
        :description (str "Vixu.com basic subscription from "
                          "2012/16/11 to 2013/16/11")
        :full-amount 1188.0}
       {:type :-
        :description "10% discount for yearly payment"
        :full-amount 1188.0
        :discount 0.1}
       {:type :+
        :description "Custom development example.com"
        :full-amount 2500.0}]
      :company-name "ACME Inc"
      :name "John Doe"
      :street "IJskelderstraat 30"
      :postal-code "5046NK"
      :city "Tilburg"
      :county "The Netherlands")
     "/home/fmw/Documents/invoices/invoice-website-acme.pdf")

.. _`Apache FOP`: http://xmlgraphics.apache.org/fop/
.. _`XSL-FO`: http://en.wikipedia.org/wiki/XSL_Formatting_Objects
.. _`website-management software`: https:/github.com/fmw/vix
.. _`Vixu.com`: http:/www.vixu.com/
