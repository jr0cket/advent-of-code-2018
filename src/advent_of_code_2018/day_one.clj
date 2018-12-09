(ns advent-of-code-2018.day-one
  (:require [advent-of-code-2018.day-one-data-set :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Day 1: Chronal Calibration
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; "We've detected some temporal anomalies," one of Santa's Elves at the Temporal Anomaly Research and Detection Instrument Station tells you. She sounded pretty worried when she called you down here. "At 500-year intervals into the past, someone has been changing Santa's history!"

;; "The good news is that the changes won't propagate to our time stream for another 25 days, and we have a device" - she attaches something to your wrist - "that will let you fix the changes with no such propagation delay. It's configured to send you 500 years further into the past every few days; that was the best we could do on such short notice."

;; "The bad news is that we are detecting roughly fifty anomalies throughout time; the device will indicate fixed anomalies with stars. The other bad news is that we only have one device and you're the best person for the job! Good lu--" She taps a button on the device and you suddenly feel like you're falling. To save Christmas, you need to get all fifty stars by December 25th.

;; Collect stars by solving puzzles. Two puzzles will be made available on each day in the advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

;; After feeling like you've been falling for a few minutes, you look at the device's tiny screen. "Error: Device must be calibrated before first use. Frequency drift detected. Cannot maintain destination lock." Below the message, the device shows a sequence of changes in frequency (your puzzle input). A value like +6 means the current frequency increases by 6; a value like -3 means the current frequency decreases by 3.

;; For example, if the device displays frequency changes of +1, -2, +3, +1, then starting from a frequency of zero, the following changes would occur:

;; Current frequency  0, change of +1; resulting frequency  1.
;; Current frequency  1, change of -2; resulting frequency -1.
;; Current frequency -1, change of +3; resulting frequency  2.
;; Current frequency  2, change of +1; resulting frequency  3.
;; In this example, the resulting frequency is 3.

;; Here are other example situations:

;; +1, +1, +1 results in  3
;; +1, +1, -2 results in  0
;; -1, -2, -3 results in -6
;; Starting with a frequency of zero, what is the resulting frequency after all of the changes in frequency have been applied?


;; Analyse the problem
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Start with nothing and add together all the numbers that represent frequency changes

;; Example data
(+ 1 1 1)
(+ 1 1 -2)
(+ -1 -2 -3)

;; so we could have all the numbers as arguments
;; usually we would have a sequence of numbers as a collection, usually a vector

;; NOTE: Added Advent of Code data set in its own namespace, as its about 1000 long
;; The data set is called frequency-changes
frequency-changes

;; Can we simply use the + function to add values in the collection?

(+ frequency-changes)

;; No, my linter tells me + can only take numbers as arguments and not a collection.
;; I get the same error message if I evaluate.

;; I could be sneaky and just paste all the numbers in as arguments to +
;; or I could use my editor to evaluate frequency-changes and replace the symbol with its value.


;; working with collections
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; we have used map function over collections before
(map + frequency-changes)

;; reduce is more applicable, so is apply
(reduce + frequency-changes)

(apply + frequency-changes)

;; Supplied answer
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(reduce + frequency-changes)
;; => 400


;; Apply or Reduce - performance testing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Does it matter if we use apply or reduce?
;; Let's measure their execution time and see
;; if there is a performance difference.

;; clojure.core/time is a macro that will evaluate an expression,
;; return the result of the expression,
;; and also return the time to execute the expression (show in standard out)

(time
 (reduce + frequency-changes))
;; => 400
;; => "Elapsed time: 0.217616 msecs"

(time
 (apply + frequency-changes))
;; => 400
;; => "Elapsed time: 0.165076 msecs"


;; Criterium is a library that generates more accurate performance measurements
;; - the Java Virtual Machine is warmed up before executing, giving consistent results
;; - an expression is executed thousands of times and average times are returned.

;; Criterium has been added as a dependency to the project.clj configuration
;; Require criterium directly into the namespace so functions can be used by name
(require '[criterium.core :refer :all ])

;; bench is a criterium function that runs a benchmark for the given expression

(bench
 (reduce + frequency-changes))

;; Evaluation count : 2111100 in 60 samples of 35185 calls.
;; Execution time mean : 27.838289 µs
;; Execution time std-deviation : 1.079367 µs
;; Execution time lower quantile : 26.730229 µs ( 2.5%)
;; Execution time upper quantile : 30.460933 µs (97.5%)
;; Overhead used : 9.182936 ns

;; Found 2 outliers in 60 samples (3.3333 %)
;; low-severe  2 (3.3333 %)
;; Variance from outliers : 25.4348 % Variance is moderately inflated by outliers

(bench
 (apply + frequency-changes))
;; Evaluation count : 1870200 in 60 samples of 31170 calls.
;; Execution time mean : 32.650124 µs
;; Execution time std-deviation : 607.046803 ns
;; Execution time lower quantile : 31.962078 µs ( 2.5%)
;; Execution time upper quantile : 33.946503 µs (97.5%)
;; Overhead used : 9.182936 ns

;; Found 2 outliers in 60 samples (3.3333 %)
;; low-severe  1 (1.6667 %)
;; low-mild  1 (1.6667 %)
;; Variance from outliers : 7.8102 % Variance is slightly inflated by outliers




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Part Two
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; You notice that the device repeats the same frequency change list over and over. To calibrate the device, you need to find the first frequency it reaches twice.

;; For example, using the same list of changes above, the device would loop as follows:

;; Current frequency  0, change of +1; resulting frequency  1.
;; Current frequency  1, change of -2; resulting frequency -1.
;; Current frequency -1, change of +3; resulting frequency  2.
;; Current frequency  2, change of +1; resulting frequency  3.
;; (At this point, the device continues from the start of the list.)
;; Current frequency  3, change of +1; resulting frequency  4.
;; Current frequency  4, change of -2; resulting frequency  2, which has already been seen.
;; In this example, the first frequency reached twice is 2. Note that your device might need to repeat its list of frequency changes many times before a duplicate frequency is found, and that duplicates might be found while in the middle of processing the list.

;; Here are other examples:

;; +1, -1 first reaches 0 twice.
;; +3, +3, +4, -2, -4 first reaches 10 twice.
;; -6, +3, +8, +5, -6 first reaches 5 twice.
;; +7, +7, -2, -7, -4 first reaches 14 twice.


;; Analysing the problem
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Part two is tricky as we need to manage state while we process the frequency-changes
;; We need to keep track of the frequencies that we adjust the device to each time.


;; imperative and mutable solution (DONT DO THIS AT HOME!)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; this is an approach that may be taken by someone coming
;; from an imperative language background (eg. C, Java).
;; The solution works, but its not a good use of Clojure.

;; using a mutable collection
(import '(java.util HashSet))
;; => java.util.HashSet

(let [result                (atom nil)
      seen-frequencies      (HashSet.)
      adjusted-frequency    (atom 0)]
  (while (nil? @result)
    (doseq [frequency frequency-changes]
      (when (and (.contains seen-frequencies @adjusted-frequency)
                 (nil? @result))
        (reset! result @adjusted-frequency)) ; break

      ; else
      (.add seen-frequencies @adjusted-frequency)
      (swap! adjusted-frequency + frequency)))
  @result)


