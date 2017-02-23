/**
 * Created by amoghpalnitkar on 2/14/17.
 */

mitraPortal
    .provider('loginModalState', function($stateProvider) {
        var provider = this;
        this.$get = function() {
            return provider;
        };
        this.state = function(stateName, options) {
            var modalInstance;
            $stateProvider.state(stateName, {
                url: options.url,
                onEnter: function($modal, $state) {
                    modalInstance = $modal.open(options);
                    modalInstance.result['finally'](function() {
                        modalInstance = null;
                        if ($state.$current.name === stateName) {
                            $state.go('^');
                        }
                    });
                },
                onExit: function() {
                    if (modalInstance) {
                        modalInstance.close();
                    }
                }
            });
        };
    });
