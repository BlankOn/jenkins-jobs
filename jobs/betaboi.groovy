String app = "WebStatic"

folder("${app}") {
  description "ini adalah description"
}

job("${app}/Beta") {
    description "web boi utama"
    logRotator {
        daysToKeep(2)
        numToKeep(2)
    }
    scm {
        git {
            remote {
                url('https://github.com/BlankOn/blankon-linux-static-web.git')
            }
            branch('master')
        }
    }
    triggers {
        scm('H/5 * * * *')
    } 

    steps {
     	shell('''node -v && npm -v
npm i
npm run build
tar -czvf dist.tar.gz dist
rsync -avz --rsh="ssh -p2222" --progress dist/ situs@waljinah.blankon.in:/home/situs/beta/dist/
echo "sukses"''')
    }
    publishers{
        archiveArtifacts {
            pattern("dist.tar.gz")
            onlyIfSuccessful()
        }
        postBuildScripts {
            onlyIfBuildSucceeds()
            steps {
                shell('curl --data chat_id=-214965156 --data-urlencode "text=Jenkins: Deploy Beta Sukses" "https://api.telegram.org/bot${token}/sendMessage"')
            }
        }
    }
}

job("${app}/Manokwari") {
    description "Manokwari.blankonlinux.or.id"
    logRotator {
        daysToKeep(2)
        numToKeep(2)
    }
    scm {
        git {
            remote {
                url('https://github.com/BlankOn/manokwari-boi.git')
            }
            branch('master')
        }
    }
    triggers {
        scm('H/5 * * * *')
    } 

    steps {
        shell('''ssh -p2222 situs@waljinah.blankon.in "cd ~/manokwari && git pull origin master"
echo "sukses"''')
    }
    publishers{
        postBuildScripts {
            onlyIfBuildSucceeds()
            steps {
                shell('curl --data chat_id=-214965156 --data-urlencode "text=Jenkins: Deploy manowari.boi Sukses" "https://api.telegram.org/bot${token}/sendMessage"')
            }
        }
    }
}
