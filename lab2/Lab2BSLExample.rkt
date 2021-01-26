;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname Lab2BSLExample) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")) #f)))
;; A MOT (ModeOfTransportation) is one of
;; -- Bicycle
;; -- Car
 
;; A Bicycle is a (make-bicycle String)
(define-struct bicycle (brand))
 
;; A Car is a (make-car String Number)
(define-struct car (make mpg))
 
;; A Person is a (make-person String MOT)
(define-struct person (name mot))
 
(define diamondback (make-bicycle "Diamondback"))
(define toyota (make-car "Toyota" 30))
(define lamborghini (make-car "Lamborghini" 17))
 
(define bob (make-person "Bob" diamondback))
(define ben (make-person "Ben" toyota))
(define becca (make-person "Becca" lamborghini))

;; Does this person's mode of transportation meet the given fuel
;; efficiency target (in miles per gallon)?
;; Person Int -> Boolean