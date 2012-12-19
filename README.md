# clj-sorted-util

A Clojure library to work with sorted collections.

## Usage

Right now the only operations implemented are concat-sorted and concat-sorted-by. Both return a lazy sequence and expect the collections to be concatenated to be sorted.

```clojure

    user> (use 'clj-sorted-util.util)

    user> (concat-sorted [[1 3 4] [1 3]])
    [1 3 4]

    user> (concat-sorted > [[5 3 1] [4 0]])
    [5 4 3 1 0]

    user> (concat-sorted-by :age [[{:age 2 :who :joe} {:age 20 :who :tom}]
                                 [{:age 1 :who :dan} {:age 22 :who :foo}]])
    [{:age 1 :who :dan} {:age 2 :who :joe} {:age 20 :who :tom} {:age 22 :who :foo}]
```


## License

Copyright © 2012 Daniel Lebrero

Distributed under the Eclipse Public License, the same as Clojure.
