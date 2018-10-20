# vrepl

Bootstrap vim with a clojure nrepl session.

## dependencies 

- [clojure 1.9+](https://clojure.org/guides/getting_started)
- [tpope/fireplace.vim](https://github.com/tpope/vim-fireplace)

## recommended plugins

- [luochen1990/rainbow](https://github.com/luochen1990/rainbow)
- [vim-scripts/paredit.vim](https://github.com/vim-scripts/paredit.vim)
- [venantius/vim-cljfmt](https://github.com/venantius/vim-cljfmt)

## setup

In `$HOME/.clojure/deps.edn`, add a new alias:

```clojure
:aliases
{:vrepl {:main-opts ["-m vrepl.core"]
         :extra-deps
         {vrepl {:git/url "https://github.com/djblue/vrepl.git"                                        
                 :sha "ecca2790b2079edaee0110d39cde46d7c771f9fd"}}}}
```

then run:

    clojure -Avrepl

You should be put into a new empty vim window in a clojure buffer with a
connected nrepl session. Any additional arguments will be passed directly
to vim, so you can pass files you want to open as well.
