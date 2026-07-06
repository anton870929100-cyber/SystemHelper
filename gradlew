d "$APP_HOME")
    CLASSPATH=$(cygpath --path --mixed "$CLASSPATH")

    JAVACMD=$(cygpath --unix "$JAVACMD")

    # Now convert the arguments - kludge to limit ourselves to /bin/sh
    for arg do
        if
            case $arg in                                #(
              -*)   false ;;                            # don't mess with options #(
              /?*)  t=${arg#/} t=/${t%%/*}              # looks like a POSIX filepath
                    [ -e "$t" ] ;;                      #(
              *)    false ;;
            esac
        then
            arg=$(cygpath --path --ignore --mixed "$arg")
        fi
        # Roll the args list around exactly as many times as the number of
        # args, so each arg winds up back in the position where it started, but
        # possibly modified.
        #
        # NB: a `for` loop evaluates its arg list each iteration, so $arg will
        # be reset to the first argument for each iteration, if it changes.
        # Therefore, we need to use a while loop.
        i=0
        for arg in "$@"; do
            if [ $i -gt 0 ] && [ "$arg" != "--" ] ; then
                args="$args \"$arg\""
            else
                args="$args $arg"
            fi
            i=$((i + 1))
        done
        set -- $args
    done
fi

# Collect the arguments
args=""
for arg in "$@"; do
    args="$args \"$arg\""
done
set -- $args

# Add default JVM options
set -- $DEFAULT_JVM_OPTS "$@"

# Add the classpath
set -- -classpath "$CLASSPATH" "$@"

# Add the main class
set -- org.gradle.wrapper.GradleWrapperMain "$@"

# Execute Gradle
exec "$JAVACMD" "$@"-mixe
