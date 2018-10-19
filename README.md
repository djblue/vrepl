# vrepl

Bootstrap vim with a clojure nrepl session.

## dependencies 

- [clojure 1.9+](https://clojure.org/guides/getting_started)
- vim with support for [`--servername`](http://vimdoc.sourceforge.net/htmldoc/remote.html)
- [fireplace.vim](https://github.com/tpope/vim-fireplace)

## setup

In `$HOME/.clojure/deps.edn`, add a new alias:

```clojure
:aliases
{:vrepl {:main-opts ["-m vrepl.core"]
         :extra-deps
         {vrepl {:git/url "https://github.com/djblue/vrepl.git"                                        
                 :sha "2214f7dff0fbe681186d573846c0b51b2efbdc3e"}}}}
```

then run:

    clojure -Avrepl

You should be put into a new empty vim window in a clojure buffer with a
connected nrepl session. Any additional arguments will be passed directly
to vim, so you can pass files you want to open as well.
